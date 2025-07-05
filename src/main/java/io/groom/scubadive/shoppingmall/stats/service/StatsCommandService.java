package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.order.domain.Order;
import io.groom.scubadive.shoppingmall.order.domain.OrderItem;
import io.groom.scubadive.shoppingmall.order.repository.OrderRepository;
import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import io.groom.scubadive.shoppingmall.stats.domain.HourlyStats;
import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import io.groom.scubadive.shoppingmall.stats.repository.DailyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.HourlyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsCommandService {

    private final OrderRepository orderRepository;
    private final HourlyStatsRepository hourlyStatsRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ProductSalesRankingRepository productSalesRankingRepository;

    @Transactional
    public void saveHourlyStats(LocalDateTime start, LocalDateTime end) {
        if (hourlyStatsRepository.existsByStartTimeAndEndTime(start, end)) {
            return; // 중복 방지
        }

        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);

        long totalSales = orders.stream().mapToLong(Order::getTotalAmount).sum();
        int totalOrders = orders.size();

        HourlyStats stats = HourlyStats.builder()
                .startTime(start)
                .endTime(end)
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .build();

        hourlyStatsRepository.save(stats);
    }

    @Transactional
    public void saveDailyStats(LocalDateTime start, LocalDateTime end, LocalDate date) {
        if (dailyStatsRepository.existsByDate(date)) return;

        List<HourlyStats> hourlyStatsList = hourlyStatsRepository.findByStartTimeBetween(start, end);

        long totalSales = hourlyStatsList.stream().mapToLong(HourlyStats::getTotalSales).sum();
        int totalOrders = hourlyStatsList.stream().mapToInt(HourlyStats::getTotalOrders).sum();

        DailyStats dailyStats = DailyStats.builder()
                .date(date)
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .build();

        dailyStatsRepository.save(dailyStats);
    }


}