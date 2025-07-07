package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.dto.request.PhoneVerifyCodeRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.PhoneVerifyRequest;
import io.groom.scubadive.shoppingmall.member.service.PhoneVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/users/phone")
public class PhoneVerificationController {

    private final PhoneVerificationService phoneService;

    @Operation(
            summary = "휴대폰 인증번호 발송",
            description = "입력한 전화번호로 인증번호를 문자로 발송합니다."
    )
    @Tag(name = "Public API", description = "비회원 공개 API")
    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto<Void>> sendCode(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "전화번호 입력 DTO", required = true)
            @Valid @RequestBody PhoneVerifyRequest requestDto
    ) {
        phoneService.sendVerificationCode(requestDto.getPhoneNumber());
        return ResponseEntity.ok(ApiResponseDto.of(200, "인증번호가 발송되었습니다.", null));
    }

    @Operation(
            summary = "휴대폰 인증번호 확인",
            description = "입력한 전화번호와 인증번호를 비교하여 인증을 완료합니다."
    )
    @Tag(name = "Public API", description = "비회원 공개 API")
    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto<Void>> verifyCode(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "휴대폰 번호 + 인증번호 입력 DTO", required = true)
            @Valid @RequestBody PhoneVerifyCodeRequest requestDto
    ) {
        phoneService.verifyCode(requestDto.getPhoneNumber(), requestDto.getCode());
        return ResponseEntity.ok(ApiResponseDto.of(200, "인증이 완료되었습니다.", null));
    }

}