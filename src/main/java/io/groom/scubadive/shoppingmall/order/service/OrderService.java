package io.groom.scubadive.shoppingmall.order.service;

import io.groom.scubadive.shoppingmall.cart.domain.Cart;
import io.groom.scubadive.shoppingmall.cart.domain.CartItem;
import io.groom.scubadive.shoppingmall.cart.repository.CartRepository;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.member.service.UserPaidService;
import io.groom.scubadive.shoppingmall.member.service.UserService;
import io.groom.scubadive.shoppingmall.order.domain.*;
import io.groom.scubadive.shoppingmall.order.dto.request.OrderCreateRequest;
import io.groom.scubadive.shoppingmall.order.dto.response.*;
import io.groom.scubadive.shoppingmall.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserPaidService userPaidService;
    private final UserService userService;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new GlobalException(ErrorCode.CART_NOT_FOUND));

        if (!cart.getUser().getId().equals(userId)) {
            throw new GlobalException(ErrorCode.FORBIDDEN);
        }

        List<Long> selectedIds = request.getCartItemIds();
        List<CartItem> selectedItems = cart.getItems().stream()
                .filter(item -> selectedIds.contains(item.getId()))
                .toList();

        if (selectedItems.isEmpty() || selectedItems.size() != selectedIds.size()) {
            throw new GlobalException(ErrorCode.INVALID_CART_ITEM);
        }

        // 주문 번호 생성
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Long todayOrderCount = orderRepository.countByCreatedAtBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
        String sequencePart = String.format("%04d", todayOrderCount + 1); // 0001, 0002, ...

        String orderNumber = "ORD" + datePart + sequencePart;

        int totalQuantity = selectedItems.stream().mapToInt(CartItem::getQuantity).sum();
        Long totalAmount = selectedItems.stream()
                .mapToLong(i -> i.getProductOption().getProduct().getPrice() * i.getQuantity())
                .sum();

        Order order = new Order(user, orderNumber, totalQuantity, totalAmount, OrderStatus.PAYMENT_COMPLETED);

        selectedItems.forEach(i -> {
            OrderItem item = new OrderItem(i.getProductOption(), i.getQuantity());
            order.addItem(item);
        });

        orderRepository.save(order);
        cart.getItems().removeIf(item -> selectedIds.contains(item.getId()));

        return mapToOrderResponse(order);
    }




    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ORDER_NOT_FOUND));
        return mapToOrderResponse(order);
    }

    public OrderListResponse getUserOrders(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        Page<Order> orders = orderRepository.findAllByUserId(user.getId(),
                PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt")));
      
        return OrderListResponse.builder()
                .page(page)
                .totalPages(orders.getTotalPages())
                .orders(orders.getContent().stream().map(o -> OrderListResponse.OrderSummary.builder()
                        .orderId(o.getId())
                        .orderNumber(o.getOrderNumber())
                        .totalAmount(o.getTotalAmount())
                        .totalQuantity(o.getTotalQuantity())
                        .orderStatus(o.getStatus().name())
                        .orderedAt(o.getCreatedAt())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public OrderListResponse getAllOrders(int page, int size) {


        Page<Order> orders = orderRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        return OrderListResponse.builder()
                .page(page)
                .totalPages(orders.getTotalPages())
                .orders(orders.getContent().stream().map(o -> OrderListResponse.OrderSummary.builder()
                        .orderId(o.getId())
                        .orderNumber(o.getOrderNumber())
                        .totalAmount(o.getTotalAmount())
                        .totalQuantity(o.getTotalQuantity())
                        .orderStatus(o.getStatus().name())
                        .orderedAt(o.getCreatedAt())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void changeStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ORDER_NOT_FOUND));

        OrderStatus currentStatus = order.getStatus();

        // 상태 전이 유효성 검증
        validateStatusTransition(currentStatus, newStatus);

        order.changeStatus(newStatus);

        // COMPLETED 상태로 변경될 때만 결제/등급 로직 실행
        if (currentStatus != OrderStatus.COMPLETED && newStatus == OrderStatus.COMPLETED) {
            Long totalAmount = order.getTotalAmount();
            Long userId = order.getUser().getId();

            userPaidService.addPayment(userId, totalAmount);
            userService.updateGradeBasedOnTotalAmount(userId);
        }
    }

    private static final Map<OrderStatus, List<OrderStatus>> VALID_TRANSITIONS = Map.of(
            OrderStatus.PAYMENT_COMPLETED, List.of(OrderStatus.CREATED, OrderStatus.CANCELED),
            OrderStatus.CREATED, List.of(OrderStatus.SHIPPING, OrderStatus.CANCELED),
            OrderStatus.SHIPPING, List.of(OrderStatus.COMPLETED)
            // COMPLETED, CANCELED 는 종료 상태
    );

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        List<OrderStatus> validNextStatuses = VALID_TRANSITIONS.getOrDefault(from, List.of());

        // 🔍 로그 출력
        log.info("🔄 현재 상태(from): {}", from);
        log.info("➡️  변경 요청 상태(to): {}", to);
        log.info("✅ 가능한 전이 상태 목록: {}", validNextStatuses);
        log.info("🧐 포함 여부 체크: {}", validNextStatuses.contains(to));

        if (!validNextStatuses.contains(to)) {
            throw new GlobalException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }
    }


    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderedAt(order.getCreatedAt())
                .userName(order.getUser().getUsername())
                .phoneNumber(order.getUser().getPhoneNumber())
                .address(order.getUser().getAddress())
                .orderStatus(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .totalQuantity(order.getTotalQuantity())
                .orderItems(order.getItems().stream().map(i -> OrderResponse.OrderItemDto.builder()
                        .productName(i.getProductOption().getProduct().getProductName())
                        .option(i.getProductOption().getColor())
                        .quantity(i.getQuantity())
                        .price(i.getProductOption().getProduct().getPrice())
                        .totalPricePerItem(i.getProductOption().getProduct().getPrice() * i.getQuantity())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId)) {
            throw new GlobalException(ErrorCode.FORBIDDEN);
        }

        if (order.getStatus() == OrderStatus.COMPLETED ) {
            throw new GlobalException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }
        else if (order.getStatus() == OrderStatus.SHIPPING) {
            throw new GlobalException(ErrorCode.ORDER_ALREADY_SHIPPING);
        }
        else if (order.getStatus() == OrderStatus.CANCELED) {
            throw new GlobalException(ErrorCode.ORDER_ALREADY_CANCELED);
        }

        order.changeStatus(OrderStatus.CANCELED);
    }
}