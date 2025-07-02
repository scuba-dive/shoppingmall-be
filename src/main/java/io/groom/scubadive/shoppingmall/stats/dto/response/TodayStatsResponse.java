package io.groom.scubadive.shoppingmall.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "오늘의 매출 및 주문 통계 응답")
public class TodayStatsResponse {

    @Schema(description = "오늘의 총 매출액", example = "150000")
    private long totalSales;

    @Schema(description = "오늘의 총 주문 수", example = "23")
    private int totalOrders;

    @Schema(description = "통계 기준 시각", example = "2025-07-02T12:00:00")
    private LocalDateTime timestamp;
}
