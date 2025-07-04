package io.groom.scubadive.shoppingmall.stats.scheduler;

import io.groom.scubadive.shoppingmall.stats.service.StatsCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsScheduler {

    private final StatsCommandService statsCommandService;

    @Scheduled(cron = "0 0 * * * *")
    public void saveHourlyStats() {
        statsCommandService.saveHourlyStats();
        log.info("Hourly stats saved.");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void saveDailyStatsAndRanking() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        statsCommandService.saveDailyStats(yesterday.atTime(23, 59, 59));
        statsCommandService.saveTopProductRankings(yesterday);
        log.info("Final daily stats and rankings saved for date: {}", yesterday);
    }
}