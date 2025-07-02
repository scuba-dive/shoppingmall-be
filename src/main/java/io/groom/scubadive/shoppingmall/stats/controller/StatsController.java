package io.groom.scubadive.shoppingmall.stats.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.stats.dto.response.RecentStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TodayStatsResponse;
import io.groom.scubadive.shoppingmall.stats.dto.response.TopProductsResponse;
import io.groom.scubadive.shoppingmall.stats.service.StatsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin API", description = "관리자 전용 API")
@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StatsController {

    private final StatsQueryService statsService;

    @Operation(summary = "오늘 통계 조회", description = "오늘의 총 매출 및 주문 수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "오늘 통계 조회 성공")
    @GetMapping("/today")
    public ResponseEntity<ApiResponseDto<TodayStatsResponse>> getTodayStats() {
        TodayStatsResponse result = statsService.getTodayStats();
        return ResponseEntity.ok(ApiResponseDto.of(200, "오늘 통계 조회 성공", result));
    }

    @Operation(summary = "최근 3일 통계 조회", description = "최근 3일간의 일별 매출 및 주문 수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "최근 통계 조회 성공")
    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDto<RecentStatsResponse>> getRecentStats() {
        RecentStatsResponse result = statsService.getRecentStats();
        return ResponseEntity.ok(ApiResponseDto.of(200, "최근 3일 통계 조회 성공", result));
    }

    @Operation(summary = "상품 판매 순위 조회", description = "오늘의 상품별 판매 순위를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 판매 순위 조회 성공")
    @GetMapping("/top-products")
    public ResponseEntity<ApiResponseDto<TopProductsResponse>> getTopProducts() {
        TopProductsResponse result = statsService.getTopProducts();
        return ResponseEntity.ok(ApiResponseDto.of(200, "오늘 상품 판매 순위 조회 성공", result));
    }
}