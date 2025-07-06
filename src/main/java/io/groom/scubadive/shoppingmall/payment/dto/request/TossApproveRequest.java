package io.groom.scubadive.shoppingmall.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TossApproveRequest {

    @Schema(description = "토스 결제키", example = "tviva20250706124007yRJN1")
    private String paymentKey;

    @Schema(description = "주문 고유 번호", example = "a8e49aa5-4101-41c0-8cc1-7344e5c471b8")
    private String orderId;

    @Schema(description = "결제 금액", example = "1133000")
    private Long amount;
}
