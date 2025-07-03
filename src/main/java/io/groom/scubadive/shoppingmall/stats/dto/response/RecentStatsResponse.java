package io.groom.scubadive.shoppingmall.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "최근 3일 통계 응답")
public class RecentStatsResponse {

    @Schema(description = "최근 3일간의 매출 및 주문 수 통계")
    private List<SalesStats> salesStats;

    @Getter
    @AllArgsConstructor
    @Schema(description = "일별 통계 데이터")
    public static class SalesStats {
        @Schema(description = "날짜", example = "2025-07-01")
        private LocalDate date;

        @Schema(description = "총 매출액", example = "125000")
        private long sales;

        @Schema(description = "총 주문 수", example = "20")
        private int orders;
    }
}
