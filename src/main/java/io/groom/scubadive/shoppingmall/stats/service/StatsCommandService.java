package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.order.domain.Order;
import io.groom.scubadive.shoppingmall.order.domain.OrderItem;
import io.groom.scubadive.shoppingmall.order.repository.OrderRepository;
import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import io.groom.scubadive.shoppingmall.stats.repository.DailyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsCommandService {

    private final OrderRepository orderRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final ProductSalesRankingRepository productSalesRankingRepository;

    public void saveDailyStats(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);

        long totalSales = orders.stream().mapToLong(Order::getTotalAmount).sum();
        int totalOrders = orders.size();

        DailyStats stats = DailyStats.builder()
                .timestamp(LocalDateTime.now())
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .build();

        dailyStatsRepository.save(stats);
    }

    public void saveTopProductRankings(LocalDate date) {
        productSalesRankingRepository.deleteByDate(date);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        List<OrderItem> items = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .toList();

        Map<String, List<OrderItem>> grouped = items.stream().collect(Collectors.groupingBy(
                item -> item.getProductOption().getProduct().getProductName()
        ));

        List<ProductSalesRanking> rankings = grouped.entrySet().stream()
                .map(entry -> {
                    String productName = entry.getKey();
                    int quantity = entry.getValue().stream().mapToInt(OrderItem::getQuantity).sum();
                    long sales = entry.getValue().stream()
                            .mapToLong(i -> i.getProductOption().getProduct().getPrice() * i.getQuantity())
                            .sum();
                    return ProductSalesRanking.builder()
                            .date(date)
                            .productName(productName)
                            .totalQuantity(quantity)
                            .totalSales(sales)
                            .build();
                })
                .sorted(Comparator.comparing(ProductSalesRanking::getTotalQuantity).reversed())
                .limit(5)
                .toList();

        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
            productSalesRankingRepository.save(rankings.get(i));
        }
    }

    public void saveHourlyStats() {
        LocalDate today = LocalDate.now();
        saveDailyStats(today);
    }

    public void saveDailyStatsAndRanking() {
        LocalDate today = LocalDate.now();
        saveDailyStats(today);
        saveTopProductRankings(today);
    }
}