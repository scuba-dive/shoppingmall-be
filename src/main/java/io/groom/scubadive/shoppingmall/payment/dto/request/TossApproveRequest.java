package io.groom.scubadive.shoppingmall.payment.dto.request;

import lombok.Getter;

@Getter
public class TossApproveRequest {
    private String paymentKey;
    private String orderId;
    private Long amount;
}
