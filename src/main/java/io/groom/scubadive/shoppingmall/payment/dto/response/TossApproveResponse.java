package io.groom.scubadive.shoppingmall.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class TossApproveResponse {

    @Schema(description = "주문번호(내부 orderId, 결제승인시 고유값)", example = "a8e49aa5-4101-41c0-8cc1-7344e5c471b8")
    private String orderId;

    @Schema(description = "토스 결제키", example = "tviva20250706124007yRJN1")
    private String paymentKey;

    @Schema(description = "결제 수단(카드, 가상계좌 등)", example = "카드")
    private String method;

    @Schema(description = "결제 승인 상태", example = "DONE")
    private String status;

    @Schema(description = "실제 승인된 금액", example = "1133000")
    private Long approvedAmount;

    @Schema(description = "승인 일시(ISO8601)", example = "2025-07-06T13:00:12+09:00")
    private String approvedAt;


    public static TossApproveResponse of(Map<String, Object> body) {
        return TossApproveResponse.builder()
                .orderId((String) body.get("orderId"))
                .paymentKey((String) body.get("paymentKey"))
                .method((String) body.get("method"))
                .status((String) body.get("status"))
                .approvedAmount(body.get("approvedAmount") != null ? Long.parseLong(body.get("approvedAmount").toString()) : null)
                .approvedAt((String) body.get("approvedAt"))
                .build();
    }
}
