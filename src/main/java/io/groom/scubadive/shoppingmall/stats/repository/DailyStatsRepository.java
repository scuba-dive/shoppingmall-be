package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {

    Optional<DailyStats> findTopByOrderByTimestampDesc();

    List<DailyStats> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}