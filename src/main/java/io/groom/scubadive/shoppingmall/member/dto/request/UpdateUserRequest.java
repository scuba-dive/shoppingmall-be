package io.groom.scubadive.shoppingmall.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String nickname;

    private String phoneNumber;

    private String currentPassword;

    private String newPassword;

    private String newPasswordCheck;

}
