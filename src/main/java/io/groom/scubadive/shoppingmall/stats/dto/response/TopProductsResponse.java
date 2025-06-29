package io.groom.scubadive.shoppingmall.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TopProductsResponse {

    private List<ProductRanking> salesRankings;
    private List<ProductRanking> quantityRankings;

    @Getter
    @AllArgsConstructor
    public static class ProductRanking {
        private int rank;
        private String productName;
        private long totalPrice;
        private int totalQuantity;
    }
}