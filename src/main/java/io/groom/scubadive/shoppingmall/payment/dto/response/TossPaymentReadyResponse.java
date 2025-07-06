package io.groom.scubadive.shoppingmall.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentReadyResponse {
    private String orderId;
    private Long amount;
    private String orderName;
    private String customerName;
}

