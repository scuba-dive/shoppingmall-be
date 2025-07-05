package io.groom.scubadive.shoppingmall.stats.scheduler;

import io.groom.scubadive.shoppingmall.stats.service.StatsCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsScheduler {

    private final StatsCommandService statsCommandService;

    // 매 30분마다 (ex: 00:30, 01:00 ...)
    @Scheduled(cron = "0 0,30 * * * *")
//    @Scheduled(cron = "*/10 * * * * *")
    public void saveCurrentHourlyStats() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime start = now.minusMinutes(30);
        LocalDateTime end = now.minusNanos(1);

        try {
            statsCommandService.saveHourlyStats(start, end);
            log.info("[스케줄러] ✅ {} ~ {} 매출 저장 성공", start, end);
        } catch (Exception e) {
            log.error("[스케줄러] ❌ {} ~ {} 매출 저장 실패", start, end, e);
        }
    }

    // 자정에 전날 23:30 ~ 23:59 저장
    @Scheduled(cron = "0 0 0 * * *")
    public void saveLastHourBeforeMidnight() {
        LocalDateTime start = LocalDate.now().minusDays(1).atTime(23, 30);
        LocalDateTime end = start.withMinute(59).withSecond(59).withNano(999_999_999);

        try {
            statsCommandService.saveHourlyStats(start, end);
            log.info("[스케줄러] ✅ 전날 마지막 시간대 매출 저장 완료: {} ~ {}", start, end);
        } catch (Exception e) {
            log.error("[스케줄러] ❌ 전날 마지막 시간대 매출 저장 실패: {} ~ {}", start, end, e);
        }
    }

    @Scheduled(cron = "0 5 0 * * *") // 매일 자정 5분 후 실행
    public void saveDailyStatsFromHourlyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime start = yesterday.atStartOfDay();         // 어제 00:00
        LocalDateTime end = yesterday.atTime(23, 59, 59, 999_999_999); // 어제 23:59:59.999999999

        try {
            statsCommandService.saveDailyStats(start, end, yesterday);
            log.info("[스케줄러] ✅ 어제 일일 매출 저장 완료: {}", yesterday);
        } catch (Exception e) {
            log.error("[스케줄러] ❌ 어제 일일 매출 저장 실패: {}", yesterday, e);
        }
    }

    @Scheduled(cron = "0 0,30 * * * *") // 매 시각 00분, 30분마다 실행
//    @Scheduled(cron = "*/10 * * * * *")
    public void updateTodayProductRankings() {
        LocalDate today = LocalDate.now();
        try {
            statsCommandService.saveTopProductRankings(today);
            log.info("[스케줄러] ✅ 오늘의 상품 판매 랭킹 저장 완료: {}", today);
        } catch (Exception e) {
            log.error("[스케줄러] ❌ 오늘의 상품 판매 랭킹 저장 실패: {}", today, e);
        }
    }
}