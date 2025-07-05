package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import io.groom.scubadive.shoppingmall.stats.domain.HourlyStats;
import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TodayStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TopProductsResponse;
import io.groom.scubadive.shoppingmall.stats.repository.HourlyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsQueryService {

    private final HourlyStatsRepository hourlyStatsRepository;
    private final ProductSalesRankingRepository productSalesRankingRepository;

    public TodayStatsResponse getTodayStats() {
        LocalDateTime start = LocalDate.now().atStartOfDay();         // 오늘 00:00
        LocalDateTime end = LocalDateTime.now();                      // 지금 시각

        List<HourlyStats> stats = hourlyStatsRepository.findByStartTimeBetween(start, end);

        long totalSales = stats.stream().mapToLong(HourlyStats::getTotalSales).sum();
        int totalOrders = stats.stream().mapToInt(HourlyStats::getTotalOrders).sum();

        return new TodayStatsResponse(totalSales, totalOrders, end);
    }


//    public RecentStatsResponse getRecentStats() {
//        LocalDate today = LocalDate.now();
//        List<DailyStats> recent = dailyStatsRepository.findByTimestampBetween(
//                today.minusDays(3).atStartOfDay(),  // 오늘 포함 최근 3일
//                today.atTime(LocalTime.MAX)
//        );
//
//        if (recent.isEmpty()) {
//            throw new GlobalException(ErrorCode.STATS_NOT_FOUND);
//        }
//
//        List<RecentStatsResponse.SalesStats> list = recent.stream()
//                .map(stat -> new RecentStatsResponse.SalesStats(
//                        stat.getTimestamp().toLocalDate(),
//                        stat.getTotalSales(),
//                        stat.getTotalOrders()))
//                .toList();
//
//        return new RecentStatsResponse(list);
//    }
//
//    public TopProductsResponse getTopProducts() {
//        LocalDate today = LocalDate.now();
//
//        List<ProductSalesRanking> salesRankingsRaw =
//                productSalesRankingRepository.findByDateOrderByTotalSalesDesc(today);
//
//        List<ProductSalesRanking> quantityRankingsRaw =
//                productSalesRankingRepository.findByDateOrderByTotalQuantityDesc(today);
//
//        if (salesRankingsRaw.isEmpty() && quantityRankingsRaw.isEmpty()) {
//            throw new GlobalException(ErrorCode.PRODUCT_SALES_RANKING_NOT_FOUND);
//        }
//
//        List<TopProductsResponse.ProductRanking> salesRankings = salesRankingsRaw.stream()
//                .limit(5)
//                .map(r -> new TopProductsResponse.ProductRanking(
//                        r.getRanking(),
//                        r.getProductName(),
//                        r.getTotalSales(),
//                        r.getTotalQuantity()))
//                .toList();
//
//        List<TopProductsResponse.ProductRanking> quantityRankings = quantityRankingsRaw.stream()
//                .limit(5)
//                .map(r -> new TopProductsResponse.ProductRanking(
//                        r.getRanking(),
//                        r.getProductName(),
//                        r.getTotalSales(),
//                        r.getTotalQuantity()))
//                .toList();
//
//        return new TopProductsResponse(salesRankings, quantityRankings);
//    }
}
