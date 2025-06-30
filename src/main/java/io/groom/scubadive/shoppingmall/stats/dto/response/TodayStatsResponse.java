package io.groom.scubadive.shoppingmall.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayStatsResponse {
    private long totalSales;
    private int totalOrders;
}