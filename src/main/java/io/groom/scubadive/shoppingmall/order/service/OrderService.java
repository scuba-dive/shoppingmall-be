package io.groom.scubadive.shoppingmall.order.service;

import io.groom.scubadive.shoppingmall.cart.domain.Cart;
import io.groom.scubadive.shoppingmall.cart.domain.CartItem;
import io.groom.scubadive.shoppingmall.cart.repository.CartRepository;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.order.domain.*;
import io.groom.scubadive.shoppingmall.order.dto.request.OrderCreateRequest;
import io.groom.scubadive.shoppingmall.order.dto.response.*;
import io.groom.scubadive.shoppingmall.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(User user, OrderCreateRequest request) {
        Cart cart = cartRepository.findById(request.getCartId()).orElseThrow();

        if (cart.getItems().isEmpty()) throw new IllegalStateException("장바구니가 비어있습니다.");

        String orderNumber = "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        int totalQuantity = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
        Long totalAmount = cart.getItems().stream().mapToLong(i ->
                i.getProductOption().getProduct().getPrice() * i.getQuantity()).sum();

        Order order = new Order(user, orderNumber, totalQuantity, totalAmount, OrderStatus.PAYMENT_COMPLETED);

        cart.getItems().forEach(i -> {
            OrderItem item = new OrderItem(i.getProductOption(), i.getQuantity());
            order.addItem(item);
        });

        orderRepository.save(order);
        cart.clearItems();

        return mapToOrderResponse(order);
    }

    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return mapToOrderResponse(order);
    }

    public OrderListResponse getUserOrders(User user, int page, int size) {
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
        order.changeStatus(status);
    }

    @Transactional
    public void cancelOrderByUser(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("해당 주문은 취소할 수 없습니다.");
        }

        if (order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("해당 주문은 취소할 수 없습니다.");
        }

        order.changeStatus(OrderStatus.CANCELED);
    }

    @Transactional
    public void cancelOrderByAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("해당 주문은 취소할 수 없습니다.");
        }

        order.changeStatus(OrderStatus.CANCELED);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderedAt(order.getCreatedAt())
                .userName(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
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