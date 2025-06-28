package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.dto.response.DailyStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.ProductRankingResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.ProductRankingResponse.ProductRank;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse.DailyStat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StatsRepository {

    @PersistenceContext
    private final EntityManager em;

    public DailyStatsResponse getTodayStats() {
        Object[] result = (Object[]) em.createQuery(
                "SELECT COALESCE(SUM(o.totalPrice), 0), COUNT(o) FROM Order o WHERE DATE(o.createdAt) = CURRENT_DATE"
        ).getSingleResult();

        Long totalSales = (Long) result[0];
        Long totalOrders = (Long) result[1];

        return new DailyStatsResponse(totalSales, totalOrders.intValue());
    }

    public RecentStatsResponse getRecentStats() {
        List<Object[]> resultList = em.createQuery(
                "SELECT DATE(o.createdAt), COALESCE(SUM(o.totalPrice), 0), COUNT(o) " +
                        "FROM Order o WHERE o.createdAt >= CURRENT_DATE - 2 GROUP BY DATE(o.createdAt) ORDER BY DATE(o.createdAt)"
        ).getResultList();

        List<DailyStat> stats = resultList.stream()
                .map(r -> new DailyStat(String.valueOf(r[0]), (Long) r[1], ((Long) r[2]).intValue()))
                .collect(Collectors.toList());

        return new RecentStatsResponse(stats);
    }

    public ProductRankingResponse getTopProductsBy(String criteria) {
        String orderBy = "quantity".equals(criteria) ? "SUM(oi.quantity) DESC" : "SUM(p.price * oi.quantity) DESC";
        String selectField = "quantity".equals(criteria) ? "SUM(oi.quantity)" : "SUM(p.price * oi.quantity)";

        List<Object[]> resultList = em.createQuery(
                "SELECT p.name, SUM(oi.quantity), SUM(p.price * oi.quantity) " +
                        "FROM OrderItem oi JOIN oi.productOption po JOIN po.product p JOIN oi.order o " +
                        "WHERE DATE(o.createdAt) = CURRENT_DATE GROUP BY p.name ORDER BY " + orderBy
        ).setMaxResults(5).getResultList();

        List<ProductRank> rankings = resultList.stream()
                .map((r, i) -> new ProductRank(resultList.indexOf(r) + 1, (String) r[0], ((Long) r[1]).intValue(), (Long) r[2]))
                .collect(Collectors.toList());

        return new ProductRankingResponse(rankings);
    }
}
