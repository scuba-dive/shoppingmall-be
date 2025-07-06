package io.groom.scubadive.shoppingmall.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "장바구니 응답 DTO")
public class CartResponse {

    @Schema(description = "장바구니 ID", example = "5")
    private Long cartId;

    @Schema(description = "장바구니에 담긴 상품 목록")
    private List<CartItemResponse> items;

    @Schema(description = "장바구니에 담긴 전체 상품 수량")
    private int totalQuantity;

    @Schema(description = "장바구니 전체 금액")
    private Long totalAmount;
}
