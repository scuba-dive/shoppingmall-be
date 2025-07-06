package io.groom.scubadive.shoppingmall.payment.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.payment.dto.request.TossPaymentRequest;
import io.groom.scubadive.shoppingmall.payment.dto.response.PaymentUrlResponse;
import io.groom.scubadive.shoppingmall.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/payments")
public class PaymentController {

    private final TossPaymentService tossPaymentService;

    @PostMapping("/toss")
    public ApiResponseDto<PaymentUrlResponse> requestTossPayment(@RequestBody TossPaymentRequest request) {
        String paymentUrl = tossPaymentService.createTossPaymentUrl(request.getCartId(), request.getCartItemIds());
        return ApiResponseDto.of(200, "결제 준비 완료", new PaymentUrlResponse(paymentUrl));
    }
}
