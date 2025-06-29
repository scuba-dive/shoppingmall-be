package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TodayStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TopProductsResponse;
import io.groom.scubadive.shoppingmall.stats.repository.DailyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsQueryService {

    private final DailyStatsRepository dailyStatsRepository;
    private final ProductSalesRankingRepository productSalesRankingRepository;

    public TodayStatsResponse getTodayStats() {
        DailyStats stats = dailyStatsRepository.findByDate(LocalDate.now());
        return new TodayStatsResponse(stats.getTotalSales(), stats.getTotalOrders());
    }

    public RecentStatsResponse getRecentStats() {
        LocalDate today = LocalDate.now();
        List<DailyStats> recent = dailyStatsRepository.findByDateBetween(today.minusDays(3), today.minusDays(1));

        List<RecentStatsResponse.SalesStats> list = recent.stream()
                .map(stat -> new RecentStatsResponse.SalesStats(
                        stat.getDate(),
                        stat.getTotalSales(),
                        stat.getTotalOrders()))
                .collect(Collectors.toList());

        return new RecentStatsResponse(list);
    }

    public TopProductsResponse getTopProducts() {
        LocalDate today = LocalDate.now();
        List<ProductSalesRanking> rankings = productSalesRankingRepository.findByDateOrderByTotalSalesDesc(today);

        List<TopProductsResponse.ProductRanking> salesRankings = rankings.stream()
                .sorted((a, b) -> Long.compare(b.getTotalSales(), a.getTotalSales()))
                .limit(5)
                .map(r -> new TopProductsResponse.ProductRanking(r.getRank(), r.getProductName(), r.getTotalSales(), r.getTotalQuantity()))
                .collect(Collectors.toList());

        List<TopProductsResponse.ProductRanking> quantityRankings = rankings.stream()
                .sorted((a, b) -> Integer.compare(b.getTotalQuantity(), a.getTotalQuantity()))
                .limit(5)
                .map(r -> new TopProductsResponse.ProductRanking(r.getRank(), r.getProductName(), r.getTotalSales(), r.getTotalQuantity()))
                .collect(Collectors.toList());

        return new TopProductsResponse(salesRankings, quantityRankings);
    }
}