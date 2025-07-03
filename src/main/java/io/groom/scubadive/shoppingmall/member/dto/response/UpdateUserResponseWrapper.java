package io.groom.scubadive.shoppingmall.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 정보 수정 결과 응답 DTO")
public class UpdateUserResponseWrapper {

    @Schema(description = "수정된 사용자 정보")
    private UserInfoResponse user;

    @Schema(description = "비밀번호 변경으로 인한 자동 로그아웃 여부", example = "true")
    private boolean loggedOut;
}
