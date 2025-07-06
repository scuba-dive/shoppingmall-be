package io.groom.scubadive.shoppingmall.payment.dto.request;

import lombok.Getter;

@Getter
public class TossPaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
}
