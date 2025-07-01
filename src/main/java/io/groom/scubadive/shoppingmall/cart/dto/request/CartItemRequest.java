package io.groom.scubadive.shoppingmall.cart.dto.request;

import lombok.Getter;

@Getter
public class CartItemRequest {
    private Long productOptionId;
    private int quantity;
}