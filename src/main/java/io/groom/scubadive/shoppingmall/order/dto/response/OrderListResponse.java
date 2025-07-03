package io.groom.scubadive.shoppingmall.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "주문 목록 응답 DTO")
public class OrderListResponse {

    @Schema(description = "현재 페이지 번호")
    private int page;

    @Schema(description = "페이지당 데이터 수")
    private int size;

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "전체 주문 수")
    private long totalElements;

    @Schema(description = "주문 요약 리스트")
    private List<OrderSummary> orders;

    @Getter
    @Builder
    @Schema(description = "주문 요약 정보")
    public static class OrderSummary {

        @Schema(description = "주문 ID", example = "1")
        private Long orderId;

        @Schema(description = "주문 번호", example = "ORDabc123456789")
        private String orderNumber;

        @Schema(description = "주문일시", example = "2025-07-02T15:30:00")
        private LocalDateTime orderedAt;

        @Schema(description = "총 수량", example = "3")
        private int totalQuantity;

        @Schema(description = "총 금액", example = "150000")
        private Long totalAmount;

        @Schema(description = "주문 상태", example = "PAYMENT_COMPLETED")
        private String orderStatus;
    }
}