package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import io.groom.scubadive.shoppingmall.stats.domain.HourlyStats;
import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TodayStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TopProductsResponse;
import io.groom.scubadive.shoppingmall.stats.repository.DailyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.HourlyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsQueryService {

    private final HourlyStatsRepository hourlyStatsRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ProductSalesRankingRepository productSalesRankingRepository;

    public TodayStatsResponse getTodayStats() {
        LocalDateTime start = LocalDate.now().atStartOfDay();         // 오늘 00:00
        LocalDateTime end = LocalDateTime.now();                      // 지금 시각

        List<HourlyStats> stats = hourlyStatsRepository.findByStartTimeBetween(start, end);

        long totalSales = stats.stream().mapToLong(HourlyStats::getTotalSales).sum();
        int totalOrders = stats.stream().mapToInt(HourlyStats::getTotalOrders).sum();

        return new TodayStatsResponse(totalSales, totalOrders, end);
    }


    @Transactional(readOnly = true)
    public RecentStatsResponse getRecentStats() {
        LocalDate today = LocalDate.now();
        LocalDate twoDaysAgo = today.minusDays(2);
        LocalDate yesterday = today.minusDays(1);

        // 어제, 그제는 DailyStats로 조회
        List<DailyStats> dailyStatsList = dailyStatsRepository.findByDateBetweenOrderByDateAsc(twoDaysAgo, yesterday);

        // 오늘은 HourlyStats로 집계
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        List<HourlyStats> todayHourlyStats = hourlyStatsRepository.findByStartTimeBetween(start, now);

        long todaySales = todayHourlyStats.stream().mapToLong(HourlyStats::getTotalSales).sum();
        int todayOrders = todayHourlyStats.stream().mapToInt(HourlyStats::getTotalOrders).sum();

        List<RecentStatsResponse.SalesStats> results = new ArrayList<>();
        for (DailyStats daily : dailyStatsList) {
            results.add(new RecentStatsResponse.SalesStats(
                    daily.getDate(),
                    daily.getTotalSales(),
                    daily.getTotalOrders()
            ));
        }

        results.add(new RecentStatsResponse.SalesStats(
                today,
                todaySales,
                todayOrders
        ));

        return new RecentStatsResponse(results);
    }


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
