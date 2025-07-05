package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.domain.HourlyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HourlyStatsRepository extends JpaRepository<HourlyStats, Long> {

    List<HourlyStats> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByStartTimeAndEndTime(LocalDateTime start, LocalDateTime end);
}

