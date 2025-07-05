package io.groom.scubadive.shoppingmall.order.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.securirty.LoginUser;
import io.groom.scubadive.shoppingmall.member.domain.User;

import io.groom.scubadive.shoppingmall.order.dto.request.OrderCreateRequest;
import io.groom.scubadive.shoppingmall.order.dto.response.OrderListResponse;
import io.groom.scubadive.shoppingmall.order.dto.response.OrderResponse;
import io.groom.scubadive.shoppingmall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/orders")
@Tag(name = "User API", description = "회원 전용 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "장바구니 ID를 기반으로 주문을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<OrderResponse>> createOrder(
            @LoginUser Long userId,
            @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(userId, request);
        return ResponseEntity.status(201).body(ApiResponseDto.of(201, "주문 생성 성공", response));
    }

    @Operation(summary = "주문 단건 조회", description = "주문 ID로 주문 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 조회 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<OrderResponse>> getOrder(@PathVariable Long id) {
        OrderResponse response = orderService.getOrder(id);
        return ResponseEntity.ok(ApiResponseDto.of(200, "주문 조회 성공", response));
    }

    @Operation(summary = "사용자 주문 목록 조회", description = "현재 로그인한 사용자의 주문 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<OrderListResponse>> getUserOrders(
            @LoginUser Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderListResponse response = orderService.getUserOrders(userId, page, size);
        return ResponseEntity.ok(ApiResponseDto.of(200, "사용자 주문 목록 조회 성공", response));
    }

    @Operation(summary = "주문 취소", description = "주문 ID로 주문을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            @ApiResponse(responseCode = "400", description = "이미 취소된 주문이거나 잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(
            @LoginUser Long userId,
            @PathVariable Long id) {
        orderService.cancelOrder(userId, id);
        return ResponseEntity.ok(ApiResponseDto.of(200, "주문 취소 성공", null));
    }
}

