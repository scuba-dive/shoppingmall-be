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
    private LocalDateTime orderDate;
    private int totalCount;
    private Long totalPrice;
    private OrderStatus status;
    private List<OrderItemDto> items;

    @Getter
    @Builder
    public static class OrderItemDto {
        private String productName;
        private String color;
        private int quantity;
        private Long price;
    }
}