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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://shoppingmall-fe-iota.vercel.app"
        },
        allowCredentials = "true"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/payments")
@Tag(name = "User API", description = "회원 전용 API")
public class PaymentController {

    private final TossPaymentService tossPaymentService;
    private final UserRepository userRepository;

    @Operation(
            summary = "Toss 결제 준비 요청",
            description = "장바구니 ID와 선택 상품 ID로 결제정보(orderId, amount 등)를 발급받고, 결제창을 띄우기 위한 준비를 합니다.\n\n"
                    + "**반환값**: orderId(주문번호), amount(결제금액), orderName, customerName"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 준비 성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TossPaymentReadyResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/toss")
    public ApiResponseDto<TossPaymentReadyResponse> requestTossPayment(
            @LoginUser Long userId,
            @RequestBody TossPaymentRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        String username = user.getUsername();

        TossPaymentReadyResponse response = tossPaymentService.createTossPaymentReady(
                userId,
                request.getCartId(),
                request.getCartItemIds(),
                username
        );
        return ApiResponseDto.of(200, "결제 준비 완료", response);
    }


    @Operation(
            summary = "Toss 결제 승인",
            description = "토스 결제 성공 후, 결제 승인(paymentKey, orderId, amount)을 요청하여 실제 주문을 생성합니다.\n\n"
                    + "성공 시 결제 및 주문이 DB에 반영됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 승인 성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TossApproveResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 결제 승인 실패")
    })
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
