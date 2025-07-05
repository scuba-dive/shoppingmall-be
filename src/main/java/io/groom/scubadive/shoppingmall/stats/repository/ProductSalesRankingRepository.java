package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.domain.ProductSalesRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesRankingRepository extends JpaRepository<ProductSalesRanking, Long> {
    List<ProductSalesRanking> findByDate(LocalDate date);

    List<ProductSalesRanking> findByDateOrderByTotalQuantityDesc(LocalDate date);
    void deleteByDate(LocalDate date);
}