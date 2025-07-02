package io.groom.scubadive.shoppingmall.order.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.order.domain.OrderStatus;
import io.groom.scubadive.shoppingmall.order.dto.response.OrderListResponse;
import io.groom.scubadive.shoppingmall.order.dto.response.OrderResponse;
import io.groom.scubadive.shoppingmall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
        },
        allowCredentials = "true"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin API", description = "관리자 전용 API")
public class OrderAdminController {

    private final OrderService orderService;

    @Operation(summary = "전체 주문 목록 조회", description = "모든 사용자의 주문 목록을 페이징 형태로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 주문 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<OrderListResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        OrderListResponse response = orderService.getAllOrders(page, size);
        return ResponseEntity.ok(ApiResponseDto.of(200, "전체 주문 목록 조회 성공", response));
    }

    @Operation(summary = "주문 상세 조회", description = "주문 ID로 주문 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<OrderResponse>> getOrderDetail(@PathVariable Long id) {
        OrderResponse response = orderService.getOrder(id);
        return ResponseEntity.ok(ApiResponseDto.of(200, "주문 상세 조회 성공", response));
    }

    @Operation(summary = "주문 상태 변경", description = "주문의 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseDto<Void>> changeOrderStatus(
            @PathVariable Long id,
            @RequestBody StatusRequest request) {
        orderService.changeStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponseDto.of(200, "주문 상태가 변경되었습니다.", null));
    }

    @Getter
    public static class StatusRequest {
        private OrderStatus status;
    }
}
