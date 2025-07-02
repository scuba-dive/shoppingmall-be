// StatsQueryService.java
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
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsQueryService {

    private final DailyStatsRepository dailyStatsRepository;
    private final ProductSalesRankingRepository productSalesRankingRepository;

    public TodayStatsResponse getTodayStats() {
        DailyStats stats = dailyStatsRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new IllegalStateException("오늘 통계가 존재하지 않습니다."));

        return new TodayStatsResponse(stats.getTotalSales(), stats.getTotalOrders(), stats.getTimestamp());
    }

    public RecentStatsResponse getRecentStats() {
        LocalDate today = LocalDate.now();
        List<DailyStats> recent = dailyStatsRepository.findByTimestampBetween(
                today.minusDays(3).atStartOfDay(),
                today.minusDays(1).atTime(LocalTime.MAX)
        );

        List<RecentStatsResponse.SalesStats> list = recent.stream()
                .map(stat -> new RecentStatsResponse.SalesStats(
                        stat.getTimestamp().toLocalDate(),  // 수정 포인트
                        stat.getTotalSales(),
                        stat.getTotalOrders()))
                .collect(Collectors.toList());

        return new RecentStatsResponse(list);
    }

    public TopProductsResponse getTopProducts() {
        LocalDate today = LocalDate.now();

        List<ProductSalesRanking> salesRankingsRaw = productSalesRankingRepository.findByDateOrderByTotalSalesDesc(today);
        List<ProductSalesRanking> quantityRankingsRaw = productSalesRankingRepository.findByDateOrderByTotalQuantityDesc(today);

        List<TopProductsResponse.ProductRanking> salesRankings = salesRankingsRaw.stream()
                .limit(5)
                .map(r -> new TopProductsResponse.ProductRanking(r.getRanking(), r.getProductName(), r.getTotalSales(), r.getTotalQuantity()))
                .collect(Collectors.toList());

        List<TopProductsResponse.ProductRanking> quantityRankings = quantityRankingsRaw.stream()
                .limit(5)
                .map(r -> new TopProductsResponse.ProductRanking(r.getRanking(), r.getProductName(), r.getTotalSales(), r.getTotalQuantity()))
                .collect(Collectors.toList());

        return new TopProductsResponse(salesRankings, quantityRankings);
    }
}
