package io.groom.scubadive.shoppingmall.order.dto.response;

import io.groom.scubadive.shoppingmall.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {
    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderedAt;
    private String userName;
    private ShippingAddress shippingAddress;
    private String orderStatus;
    private String paymentMethod;
    private Long totalAmount;
    private int totalCount;
    private List<OrderItemDto> orderItems;

    @Getter
    @Builder
    public static class ShippingAddress {
        private String recipient;
        private String phone;
        private String zipcode;
        private String address1;
        private String address2;
    }

    @Getter
    @Builder
    public static class OrderItemDto {
        private String productName;
        private String option;
        private int quantity;
        private Long price;
        private Long totalPrice;
    }
}