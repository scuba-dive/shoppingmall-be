package io.groom.scubadive.shoppingmall.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecentStatsResponse {

    private List<SalesStats> salesStats;

    @Getter
    @AllArgsConstructor
    public static class SalesStats {
        private LocalDate date;
        private long sales;
        private int orders;
    }
}