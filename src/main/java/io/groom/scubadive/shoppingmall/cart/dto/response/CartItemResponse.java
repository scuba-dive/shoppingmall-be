package io.groom.scubadive.shoppingmall.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private String productName;
    private String color;
    private Long price;
    private int quantity;
}