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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        List<Long> selectedIds = request.getCartItemIds();

        List<CartItem> selectedItems = cart.getItems().stream()
                .filter(item -> selectedIds.contains(item.getId()))
                .toList();

        if (selectedItems.isEmpty()) {
            throw new IllegalStateException("선택된 장바구니 항목이 없습니다.");
        }

        String orderNumber = "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
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

        // 선택된 항목만 장바구니에서 제거
        cart.getItems().removeIf(item -> selectedIds.contains(item.getId()));

        return mapToOrderResponse(order);
    }


    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return mapToOrderResponse(order);
    }

    public OrderListResponse getUserOrders(Long userId, int page, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        Page<Order> orders = orderRepository.findAllByUserId(user.getId(), PageRequest.of(page, size));
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


        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, size));
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
    public void changeStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderStatus previousStatus = order.getStatus();

        order.changeStatus(status);

        if(previousStatus != OrderStatus.COMPLETED && status == OrderStatus.COMPLETED){
            Long totalAmount = order.getTotalAmount();
            Long userId = order.getUser().getId();

            userPaidService.addPayment(userId, totalAmount); // 누적 결제 금액 반영
            userService.updateGradeBasedOnTotalAmount(userId); // 등급 갱신
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
}