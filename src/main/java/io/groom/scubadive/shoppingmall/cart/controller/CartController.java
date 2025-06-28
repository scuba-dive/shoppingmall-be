package io.groom.scubadive.shoppingmall.cart.controller;

import io.groom.scubadive.shoppingmall.cart.dto.request.CartItemRequest;
import io.groom.scubadive.shoppingmall.cart.dto.request.CartUpdateRequest;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartItemResponse;
import io.groom.scubadive.shoppingmall.cart.service.CartService;
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
    public ResponseEntity<List<CartItemResponse>> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getItems(user));
    }

    @PostMapping("/items")
    public ResponseEntity<String> addItem(@AuthenticationPrincipal User user,
                                          @RequestBody CartItemRequest request) {
        cartService.addItem(user, request);
        return ResponseEntity.status(201).body("장바구니에 추가되었습니다.");
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<String> updateItem(@PathVariable Long cartItemId,
                                             @RequestBody CartUpdateRequest request) {
        cartService.updateItem(cartItemId, request);
        return ResponseEntity.ok("수량이 수정되었습니다.");
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long cartItemId) {
        cartService.deleteItem(cartItemId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.ok("장바구니가 비워졌습니다.");
    }
}
