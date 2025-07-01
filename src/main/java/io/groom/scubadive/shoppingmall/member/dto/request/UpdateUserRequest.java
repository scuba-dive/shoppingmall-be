package io.groom.scubadive.shoppingmall.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    @Size(min = 3, max = 20, message = "닉네임은 3자 이상 20자 이하여야 합니다.")
    private String nickname;

    private String phoneNumber;

    @Size(min = 4, max = 30, message = "현재 비밀번호는 4자 이상이어야 합니다.")
    private String currentPassword;

    @Size(min = 4, max = 30, message = "새 비밀번호는 4자 이상이어야 합니다.")
    private String newPassword;

    @Size(min = 4, max = 30)
    private String newPasswordCheck;

}
