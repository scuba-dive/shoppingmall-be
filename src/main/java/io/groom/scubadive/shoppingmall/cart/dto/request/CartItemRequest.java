package io.groom.scubadive.shoppingmall.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 상품 추가 요청 DTO")
public class CartItemRequest {

    @Schema(description = "상품 옵션 ID", example = "501")
    private Long productOptionId;

    @Schema(description = "담을 수량", example = "2")
    private int quantity;
}
