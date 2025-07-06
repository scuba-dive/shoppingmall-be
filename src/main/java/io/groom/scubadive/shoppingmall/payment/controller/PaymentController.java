package io.groom.scubadive.shoppingmall.payment.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.global.securirty.LoginUser;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.payment.dto.request.TossApproveRequest;
import io.groom.scubadive.shoppingmall.payment.dto.request.TossPaymentRequest;
import io.groom.scubadive.shoppingmall.payment.dto.response.TossApproveResponse;
import io.groom.scubadive.shoppingmall.payment.dto.response.TossPaymentReadyResponse;
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
    private final UserRepository userRepository;

    @PostMapping("/toss")
    public ApiResponseDto<TossPaymentReadyResponse> requestTossPayment(
            @LoginUser Long userId,
            @RequestBody TossPaymentRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        String username = user.getUsername();

        TossPaymentReadyResponse response = tossPaymentService.createTossPaymentReady(
                request.getCartId(),
                request.getCartItemIds(),
                username
        );
        return ApiResponseDto.of(200, "결제 준비 완료", response);
    }

    @PostMapping("/toss/confirm")
    public ApiResponseDto<TossApproveResponse> confirmTossPayment(
            @RequestBody TossApproveRequest request) {

        TossApproveResponse response = tossPaymentService.approvePayment(
                request.getPaymentKey(),
                request.getOrderId(),
                request.getAmount()
        );
        return ApiResponseDto.of(200, "결제 승인 완료", response);
    }
}
