package io.groom.scubadive.shoppingmall.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderListResponse {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<OrderSummary> orders;

    @Getter
    @Builder
    public static class OrderSummary {
        private Long orderId;
        private String orderNumber;
        private LocalDateTime orderedAt;
        private int totalCount;
        private Long totalAmount;
        private String paymentStatus;
        private String deliveryStatus;
    }
}