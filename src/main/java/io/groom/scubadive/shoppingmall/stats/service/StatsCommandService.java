package io.groom.scubadive.shoppingmall.stats.service;

import io.groom.scubadive.shoppingmall.order.domain.Order;
import io.groom.scubadive.shoppingmall.order.domain.OrderItem;
import io.groom.scubadive.shoppingmall.order.repository.OrderRepository;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import io.groom.scubadive.shoppingmall.stats.domain.HourlyStats;
import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import io.groom.scubadive.shoppingmall.stats.repository.DailyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.HourlyStatsRepository;
import io.groom.scubadive.shoppingmall.stats.repository.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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


    @Transactional
    public void saveTopProductRankings(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59, 999_999_999);

        // 1. 날짜 범위 기준으로 주문 조회
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        log.info("🔍 {} 기준 주문 개수: {}", date, orders.size());

        // 2. 주문에 포함된 모든 아이템 수집
        List<OrderItem> orderItems = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .toList();
        log.info("🔎 {} 기준 OrderItem 개수: {}", date, orderItems.size());

        // 3. 상품 기준 그룹핑
        Map<Product, List<OrderItem>> grouped = orderItems.stream()
                .filter(item -> item.getProductOption() != null && item.getProductOption().getProduct() != null)
                .collect(Collectors.groupingBy(item -> item.getProductOption().getProduct()));

        log.info("🔧 상품별 그룹핑 결과: {}", grouped.keySet().stream()
                .map(Product::getProductName)
                .collect(Collectors.toList()));

        // 4. 집계 및 정렬 후 상위 5개만 추출
        List<ProductSalesRanking> rankings = grouped.entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    List<OrderItem> items = entry.getValue();

                    int totalQuantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
                    long totalSales = items.stream()
                            .mapToLong(item -> item.getQuantity() * product.getPrice())
                            .sum();

                    return ProductSalesRanking.builder()
                            .date(date)
                            .productName(product.getProductName())
                            .totalQuantity(totalQuantity)
                            .totalSales(totalSales)
                            .build();
                })
                .sorted(Comparator.comparing(ProductSalesRanking::getTotalSales).reversed())
                .limit(5)
                .collect(Collectors.toList());

        log.info("📊 총 {}개 상품의 판매 랭킹 계산 완료", rankings.size());

        // 5. 순위 부여
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRanking(i + 1);
            ProductSalesRanking r = rankings.get(i);
            log.info("🏷️ 순위 {}: {}, 수량: {}, 매출: {}", r.getRanking(), r.getProductName(), r.getTotalQuantity(), r.getTotalSales());
        }

        // 6. 기존 랭킹 삭제 후 저장
        productSalesRankingRepository.deleteByDate(date);
        productSalesRankingRepository.saveAll(rankings);
    }

}