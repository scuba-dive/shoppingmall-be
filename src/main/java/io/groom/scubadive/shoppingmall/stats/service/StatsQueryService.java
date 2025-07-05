package io.groom.scubadive.shoppingmall.stats.service;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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


    @Transactional(readOnly = true)
    public TopProductsResponse getTopProducts() {
        LocalDate today = LocalDate.now();

        // 오늘 날짜 기준 랭킹 전체 조회
        List<ProductSalesRanking> rankings = productSalesRankingRepository.findByDate(today);

        // 판매금액 기준 상위 5개
        List<TopProductsResponse.ProductRanking> salesRankings = rankings.stream()
                .sorted(Comparator.comparingLong(ProductSalesRanking::getTotalSales).reversed())
                .limit(5)
                .map(r -> new TopProductsResponse.ProductRanking(
                        r.getRanking(),
                        r.getProductName(),
                        r.getTotalSales(),
                        r.getTotalQuantity()
                ))
                .toList();

        // 판매수량 기준 상위 5개
        List<TopProductsResponse.ProductRanking> quantityRankings = rankings.stream()
                .sorted(Comparator.comparingInt(ProductSalesRanking::getTotalQuantity).reversed())
                .limit(5)
                .map(r -> new TopProductsResponse.ProductRanking(
                        r.getRanking(),
                        r.getProductName(),
                        r.getTotalSales(),
                        r.getTotalQuantity()
                ))
                .toList();

        return new TopProductsResponse(salesRankings, quantityRankings);
    }
}
