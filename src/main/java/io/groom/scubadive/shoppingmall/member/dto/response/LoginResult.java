package io.groom.scubadive.shoppingmall.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 결과 응답 DTO")
public class LoginResult {

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsIn...")
    private String accessToken;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsIn...")
    private String refreshToken;

    @Schema(description = "사용자 요약 정보")
    private UserSummary user;
}
