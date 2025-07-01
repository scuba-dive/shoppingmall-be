package io.groom.scubadive.shoppingmall.cart.controller;

import io.groom.scubadive.shoppingmall.cart.dto.request.CartItemRequest;
import io.groom.scubadive.shoppingmall.cart.dto.request.CartUpdateRequest;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartItemResponse;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartResponse;
import io.groom.scubadive.shoppingmall.cart.service.CartService;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<CartResponse>> getCart(@AuthenticationPrincipal User user) {
        CartResponse response = cartService.getCartResponse(user);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니 조회 성공", response));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponseDto<CartItemResponse>> addItem(@AuthenticationPrincipal User user,
                                                                    @RequestBody CartItemRequest request) {
        CartItemResponse response = cartService.addItem(user, request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니에 추가되었습니다.", response));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponseDto<CartItemResponse>> updateItem(@PathVariable Long cartItemId,
                                                                       @RequestBody CartUpdateRequest request) {
        CartItemResponse response = cartService.updateItem(cartItemId, request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니 수량이 수정되었습니다.", response));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteItem(@PathVariable Long cartItemId) {
        cartService.deleteItem(cartItemId);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니 항목이 삭제되었습니다.", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDto<Void>> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.ok(ApiResponseDto.of(200, "장바구니가 비워졌습니다.", null));
    }
}