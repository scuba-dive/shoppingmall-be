package io.groom.scubadive.shoppingmall.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentReadyResponse {

    @Schema(description = "주문번호 (orderId, 결제창에 전달)", example = "a8e49aa5-4101-41c0-8cc1-7344e5c471b8")
    private String orderId;

    @Schema(description = "최종 결제 금액", example = "1133000")
    private Long amount;

    @Schema(description = "대표 상품명", example = "의자1")
    private String orderName;

    @Schema(description = "고객명", example = "user1")
    private String customerName;
}

