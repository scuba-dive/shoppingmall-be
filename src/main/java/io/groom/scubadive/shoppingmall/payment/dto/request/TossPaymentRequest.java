package io.groom.scubadive.shoppingmall.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class TossPaymentRequest {

    @Schema(description = "장바구니 ID", example = "5")
    private Long cartId;

    @Schema(description = "선택한 장바구니 아이템 ID 목록", example = "[27, 28]")
    private List<Long> cartItemIds;
}

