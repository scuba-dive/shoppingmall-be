package io.groom.scubadive.shoppingmall.stats.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.stats.dto.response.DailyStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.ProductRankingResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponseDto<DailyStatsResponse>> getTodayStats() {
        return ResponseEntity.ok(ApiResponseDto.of(200, "오늘 통계 조회 성공", statsService.getTodayStats()));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDto<RecentStatsResponse>> getRecentStats() {
        return ResponseEntity.ok(ApiResponseDto.of(200, "최근 3일 통계 조회 성공", statsService.getRecentStats()));
    }

    @GetMapping("/top-products")
    public ResponseEntity<ApiResponseDto<ProductRankingResponse>> getTopProducts(@RequestParam String criteria) {
        return ResponseEntity.ok(ApiResponseDto.of(200, "오늘의 상품 판매 순위 조회 성공", statsService.getTopProducts(criteria)));
    }
}