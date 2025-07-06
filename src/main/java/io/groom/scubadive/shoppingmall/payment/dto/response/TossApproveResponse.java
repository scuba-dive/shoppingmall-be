package io.groom.scubadive.shoppingmall.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class TossApproveResponse {
    private String orderId;
    private String paymentKey;
    private String method;
    private String status;
    private Long approvedAmount;
    private String approvedAt;
    // 기타 필요시 추가
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
