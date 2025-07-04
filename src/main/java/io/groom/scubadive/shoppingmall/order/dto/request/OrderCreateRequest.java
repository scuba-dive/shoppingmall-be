package io.groom.scubadive.shoppingmall.order.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    private Long cartId;
    private List<Long> cartItemIds; // 선택된 아이템 ID 리스트 추가
}