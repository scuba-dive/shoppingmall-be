package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.dto.response.DailyStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.ProductRankingResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DailyStatsResponse getTodayStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        Tuple result = em.createQuery(
                        "SELECT COALESCE(SUM(o.totalPrice), 0), COUNT(o) " +
                                "FROM Order o " +
                                "WHERE o.createdAt BETWEEN :start AND :end", Tuple.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return new DailyStatsResponse(
                ((Number) result.get(0)).longValue(),
                ((Number) result.get(1)).intValue()
        );
    }

    @Override
    public RecentStatsResponse getRecentStats() {
        LocalDate today = LocalDate.now();

        List<RecentStatsResponse.DailyStat> stats = List.of(0, 1, 2).stream()
                .map(i -> {
                    LocalDate date = today.minusDays(i);
                    LocalDateTime start = date.atStartOfDay();
                    LocalDateTime end = date.atTime(LocalTime.MAX);

                    Tuple result = em.createQuery(
                                    "SELECT COALESCE(SUM(o.totalPrice), 0), COUNT(o) " +
                                            "FROM Order o " +
                                            "WHERE o.createdAt BETWEEN :start AND :end", Tuple.class)
                            .setParameter("start", start)
                            .setParameter("end", end)
                            .getSingleResult();

                    return new RecentStatsResponse.DailyStat(
                            date.toString(),
                            ((Number) result.get(0)).longValue(),
                            ((Number) result.get(1)).intValue()
                    );
                })
                .collect(Collectors.toList());

        return new RecentStatsResponse(stats);
    }

    @Override
    public ProductRankingResponse getTopProductRankings(String criteria) {
        String groupSelect = "SELECT oi.productOption.product.name, SUM(oi.quantity), SUM(oi.quantity * oi.productOption.product.price) ";
        String baseQuery = "FROM OrderItem oi JOIN oi.order o ";
        String filter = "WHERE o.createdAt BETWEEN :start AND :end ";
        String groupBy = "GROUP BY oi.productOption.product.name ";
        String orderBy = criteria.equals("quantity") ? "ORDER BY SUM(oi.quantity) DESC" : "ORDER BY SUM(oi.quantity * oi.productOption.product.price) DESC";

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        List<Tuple> results = em.createQuery(
                        groupSelect + baseQuery + filter + groupBy + orderBy,
                        Tuple.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .setMaxResults(5)
                .getResultList();

        List<ProductRankingResponse.ProductRank> rankings = results.stream()
                .map(tuple -> new ProductRankingResponse.ProductRank(
                        results.indexOf(tuple) + 1,
                        (String) tuple.get(0),
                        ((Number) tuple.get(1)).intValue(),
                        ((Number) tuple.get(2)).longValue()
                ))
                .collect(Collectors.toList());

        return new ProductRankingResponse(rankings);
    }
}
