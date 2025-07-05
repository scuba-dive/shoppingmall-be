package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {
    boolean existsByDate(LocalDate date);

    List<DailyStats> findByDateBetweenOrderByDateAsc(LocalDate from, LocalDate to);

}