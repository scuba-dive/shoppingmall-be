package io.groom.scubadive.shoppingmall.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecentStatsResponse {
    private List<DailyStat> salesStats;

    @Getter
    @AllArgsConstructor
    public static class DailyStat {
        private String date;
        private long sales;
        private int orders;
    }
}