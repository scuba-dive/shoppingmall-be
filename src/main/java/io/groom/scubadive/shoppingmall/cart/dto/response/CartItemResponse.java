package io.groom.scubadive.shoppingmall.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private Long productOptionId;
    private String color;
    private Long price;
    private int quantity;
    private Long totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}