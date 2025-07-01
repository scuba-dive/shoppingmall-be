package io.groom.scubadive.shoppingmall.order.dto.response;

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
    private String phoneNumber;
    private String address;
    private String orderStatus;
    private Long totalAmount;
    private int totalQuantity;
    private List<OrderItemDto> orderItems;

    @Getter
    @Builder
    public static class OrderItemDto {
        private String productName;
        private String option;
        private int quantity;
        private Long price;
        private Long totalPricePerItem;
    }
}