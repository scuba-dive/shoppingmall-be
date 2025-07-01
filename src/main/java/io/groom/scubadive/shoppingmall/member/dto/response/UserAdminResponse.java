package io.groom.scubadive.shoppingmall.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserAdminResponse {
    private Long id;
    private String username;
    private String nickname;
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String role;
    private String status;
    private String grade;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;

}
