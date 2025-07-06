package io.groom.scubadive.shoppingmall.order.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    private Long cartId;
    private List<Long> cartItemIds;

    public OrderCreateRequest(Long cartId, List<Long> cartItemIds) {
        this.cartId = cartId;
        this.cartItemIds = cartItemIds;
    }
}