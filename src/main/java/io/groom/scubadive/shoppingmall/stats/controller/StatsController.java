package io.groom.scubadive.shoppingmall.stats.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TodayStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TopProductsResponse;
import io.groom.scubadive.shoppingmall.stats.service.StatsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsQueryService statsService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponseDto<TodayStatsResponse>> getTodayStats() {
        TodayStatsResponse result = statsService.getTodayStats();
        return ResponseEntity.ok(ApiResponseDto.of(200, "오늘 통계 조회 성공", result));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDto<RecentStatsResponse>> getRecentStats() {
        RecentStatsResponse result = statsService.getRecentStats();
        return ResponseEntity.ok(ApiResponseDto.of(200, "최근 3일 통계 조회 성공", result));
    }

    @GetMapping("/top-products")
    public ResponseEntity<ApiResponseDto<TopProductsResponse>> getTopProducts() {
        TopProductsResponse result = statsService.getTopProducts();
        return ResponseEntity.ok(ApiResponseDto.of(200, "오늘 상품 판매 순위 조회 성공", result));
    }
}