package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.util.CookieUtil;
import io.groom.scubadive.shoppingmall.member.dto.response.AccessTokenResponse;
import io.groom.scubadive.shoppingmall.member.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://shoppingmall-fe-iota.vercel.app"
        },
        allowCredentials = "true"
)
@Tag(name = "Token API", description = "JWT 토큰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    private final TokenService tokenService;

    @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 이용하여 새로운 AccessToken을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "RefreshToken이 유효하지 않음", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<AccessTokenResponse>> refreshToken(HttpServletRequest request) {
        String refreshToken = CookieUtil.getRefreshTokenFromRequest(request);
        log.info("[REFRESH] 받은 refreshToken: {}", refreshToken);
        String accessToken = tokenService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(ApiResponseDto.of(200, "AccessToken 재발급 성공", new AccessTokenResponse(accessToken)));
    }
}
