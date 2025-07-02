package io.groom.scubadive.shoppingmall.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 수량 수정 요청 DTO")
public class CartUpdateRequest {

    @Schema(description = "수정할 수량", example = "3")
    private int quantity;
}
