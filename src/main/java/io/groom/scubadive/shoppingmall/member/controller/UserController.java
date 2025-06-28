package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.dto.request.SignInRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.SignInResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.UserResponse;
import io.groom.scubadive.shoppingmall.member.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "회원가입이 완료되었습니다.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<SignInResponse>> login(
            @RequestBody @Valid SignInRequest request,
            HttpServletResponse response
    ) {
        SignInResponse signInResponse = userService.login(request);

        // Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", signInResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        // 응답 데이터는 AccessToken + UserSummary
        return ResponseEntity.ok(
                ApiResponseDto.of(200, "로그인에 성공하였습니다.", signInResponse)
        );
    }
}
