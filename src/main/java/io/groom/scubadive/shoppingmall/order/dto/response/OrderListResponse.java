package io.groom.scubadive.shoppingmall.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class OrderListResponse {
    private int totalPages;
    private int currentPage;
    private List<OrderSummary> orders;

    @Getter
    @Builder
    public static class OrderSummary {
        private Long orderId;
        private String orderNumber;
        private String username;
        private Long totalPrice;
        private String status;
    }
}