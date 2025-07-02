package io.groom.scubadive.shoppingmall.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "회원 정보 수정 요청 DTO")
public class UpdateUserRequest {

    @Schema(description = "변경할 닉네임 (중복 불가)", example = "new_nickname")
    private String nickname;

    @Schema(description = "변경할 전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "현재 비밀번호 (비밀번호 변경 시 필수)", example = "currentPass123")
    private String currentPassword;

    @Schema(description = "새 비밀번호 (비밀번호 변경 시 사용)", example = "newSecurePass456")
    private String newPassword;

    @Schema(description = "새 비밀번호 확인", example = "newSecurePass456")
    private String newPasswordCheck;
}
