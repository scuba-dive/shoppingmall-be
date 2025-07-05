package io.groom.scubadive.shoppingmall.stats.repository;

import io.groom.scubadive.shoppingmall.stats.domain.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {

    Optional<DailyStats> findTopByOrderByTimestampDesc();

    List<DailyStats> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("DELETE FROM DailyStats d WHERE d.timestamp >= :start AND d.timestamp <= :end")
    void deleteByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}