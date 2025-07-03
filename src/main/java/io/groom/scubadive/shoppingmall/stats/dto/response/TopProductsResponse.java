package io.groom.scubadive.shoppingmall.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "오늘의 상품 판매 순위 응답")
public class TopProductsResponse {

    @Schema(description = "판매금액 기준 상위 상품 리스트")
    private List<ProductRanking> salesRankings;

    @Schema(description = "판매수량 기준 상위 상품 리스트")
    private List<ProductRanking> quantityRankings;

    @Getter
    @AllArgsConstructor
    @Schema(description = "상품 판매 순위 정보")
    public static class ProductRanking {

        @Schema(description = "순위", example = "1")
        private int rank;

        @Schema(description = "상품명", example = "스쿠버 장비 세트")
        private String productName;

        @Schema(description = "총 판매 금액", example = "1250000")
        private long totalSales;

        @Schema(description = "총 판매 수량", example = "18")
        private int totalQuantity;
    }
}
