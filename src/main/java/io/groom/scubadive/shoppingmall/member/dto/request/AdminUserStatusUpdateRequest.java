package io.groom.scubadive.shoppingmall.member.dto.request;

import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminUserStatusUpdateRequest {

    @NotNull(message = "상태값은 필수입니다.")
    private UserStatus status;

}
