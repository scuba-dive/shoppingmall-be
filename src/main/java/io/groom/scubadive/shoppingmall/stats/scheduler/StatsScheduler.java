package io.groom.scubadive.shoppingmall.stats.scheduler;

import io.groom.scubadive.shoppingmall.stats.service.StatsCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        statsCommandService.saveDailyStatsAndRanking();
        log.info("Daily stats and ranking saved.");
    }
}