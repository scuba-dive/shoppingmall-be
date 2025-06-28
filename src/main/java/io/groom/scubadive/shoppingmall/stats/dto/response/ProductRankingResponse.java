package io.groom.scubadive.shoppingmall.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductRankingResponse {
    private List<ProductRank> rankings;

    @Getter
    @AllArgsConstructor
    public static class ProductRank {
        private int rank;
        private String productName;
        private int totalQuantity;
        private long totalPrice;
    }
}