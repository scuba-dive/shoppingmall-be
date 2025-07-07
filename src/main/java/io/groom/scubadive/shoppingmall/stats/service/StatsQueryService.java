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
        LocalDate today = LocalDate.now();
        // 1. 오늘 날짜에 해당하는 HourlyStats 중 가장 최근 것 찾기
        List<HourlyStats> todayHourlyStats = hourlyStatsRepository.findByStartTimeBetween(
                today.atStartOfDay(),
                today.atTime(23, 59, 59, 999_999_999)
        );
        // 2. 마지막 집계 시각
        LocalDateTime lastEndTime = todayHourlyStats.stream()
                .map(HourlyStats::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        long totalSales = todayHourlyStats.stream().mapToLong(HourlyStats::getTotalSales).sum();
        int totalOrders = todayHourlyStats.stream().mapToInt(HourlyStats::getTotalOrders).sum();

        return new TodayStatsResponse(totalSales, totalOrders, lastEndTime);
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
