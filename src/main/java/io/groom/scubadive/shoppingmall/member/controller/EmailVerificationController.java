package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://shoppingmall-fe-iota.vercel.app"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(
            summary = "이메일 인증 처리",
            description = "사용자가 이메일로 받은 인증 링크를 클릭하면 호출되는 API입니다. 이메일 인증 코드를 검증한 후 프론트엔드로 리디렉션됩니다."
    )
    @Tag(name = "Public API", description = "비회원 공개 API")
    @GetMapping("/email/verify")
    public void verifyEmail(
            @Parameter(description = "이메일 인증 코드", example = "a1b2c3d4e5")
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {
        boolean success = emailVerificationService.verifyEmailCode(code);

        // 프론트엔드 인증 완료/실패 페이지로 리다이렉트
        String redirectUri;
        if (success) {
            redirectUri = "https://shoppingmall-fe-iota.vercel.app/auth/signin";
        } else {
            redirectUri = "https://shoppingmall-fe-iota.vercel.app/auth/signup";
        }
        response.sendRedirect(redirectUri);
    }
}