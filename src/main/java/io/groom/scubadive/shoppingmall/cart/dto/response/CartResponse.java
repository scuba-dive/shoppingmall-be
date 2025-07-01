package io.groom.scubadive.shoppingmall.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private List<CartItemResponse> items;
    private int totalQuantity;
    private Long totalAmount;
}