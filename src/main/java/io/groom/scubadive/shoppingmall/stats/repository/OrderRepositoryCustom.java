package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.dto.response.DailyStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.ProductRankingResponse;

public interface OrderRepositoryCustom {
    DailyStatsResponse getTodayStats();
    RecentStatsResponse getRecentStats();
    ProductRankingResponse getTopProductRankings(String criteria);
}