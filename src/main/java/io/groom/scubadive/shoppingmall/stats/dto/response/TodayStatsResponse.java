package io.groom.scubadive.shoppingmall.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodayStatsResponse {
    private long totalSales;
    private int totalOrders;
    private LocalDateTime timestamp;
}