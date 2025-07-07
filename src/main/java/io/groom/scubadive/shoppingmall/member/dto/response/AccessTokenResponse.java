package io.groom.scubadive.shoppingmall.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "AccessToken 재발급 응답 DTO")
public class AccessTokenResponse {

    @Schema(description = "재발급된 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsIn...")
    private String accessToken;
}