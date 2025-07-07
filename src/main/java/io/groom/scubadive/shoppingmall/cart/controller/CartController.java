package io.groom.scubadive.shoppingmall.cart.controller;

import io.groom.scubadive.shoppingmall.cart.dto.request.CartItemRequest;
import io.groom.scubadive.shoppingmall.cart.dto.request.CartUpdateRequest;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartItemResponse;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartResponse;
import io.groom.scubadive.shoppingmall.cart.service.CartService;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.securirty.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://shoppingmall-fe-iota.vercel.app"
        },
        allowCredentials = "true"
)
@Tag(name = "User API", description = "회원 전용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "현재 로그인한 사용자의 장바구니를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<CartResponse>> getCart(@LoginUser Long userId) {
        CartResponse response = cartService.getCartResponse(userId);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니 조회 성공", response));
    }

    @Operation(summary = "장바구니에 상품 추가", description = "상품 옵션과 수량을 장바구니에 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니에 추가되었습니다.")
    })
    @PostMapping("/items")
    public ResponseEntity<ApiResponseDto<CartItemResponse>> addItem(@LoginUser Long userId,
                                                                    @RequestBody CartItemRequest request) {
        CartItemResponse response = cartService.addItem(userId, request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니에 추가되었습니다.", response));
    }

    @Operation(summary = "장바구니 수량 수정", description = "특정 장바구니 항목의 수량을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 수량이 수정되었습니다.")
    })
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponseDto<CartItemResponse>> updateItem(@PathVariable Long cartItemId,
                                                                       @RequestBody CartUpdateRequest request) {
        CartItemResponse response = cartService.updateItem(cartItemId, request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니 수량이 수정되었습니다.", response));
    }

    @Operation(summary = "장바구니 항목 삭제", description = "특정 장바구니 항목을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 항목이 삭제되었습니다.")
    })
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteItem(@PathVariable Long cartItemId) {
        cartService.deleteItem(cartItemId);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니 항목이 삭제되었습니다.", null));
    }

    @Operation(summary = "장바구니 비우기", description = "사용자의 장바구니를 전체 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니가 비워졌습니다.")
    })
    @DeleteMapping
    public ResponseEntity<ApiResponseDto<Void>> clearCart(@LoginUser Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니가 비워졌습니다.", null));
    }
}
