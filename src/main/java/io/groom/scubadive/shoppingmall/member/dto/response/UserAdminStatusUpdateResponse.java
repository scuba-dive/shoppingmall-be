package io.groom.scubadive.shoppingmall.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.groom.scubadive.shoppingmall.member.domain.enums.Grade;
import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserAdminStatusUpdateResponse {

    private Long id;
    private String email;
    private String nickname;
    private UserStatus status;
    private Grade grade;
    private Role role;

    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
