package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.stats.dto.response.DailyStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.ProductRankingResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final OrderRepositoryCustom orderRepositoryCustom;

    public DailyStatsResponse getTodayStats() {
        return orderRepositoryCustom.getTodayStats();
    }

    public RecentStatsResponse getRecentStats() {
        return orderRepositoryCustom.getRecentStats();
    }

    public ProductRankingResponse getTopProducts(String criteria) {
        return orderRepositoryCustom.getTopProductRankings(criteria);
    }

}
