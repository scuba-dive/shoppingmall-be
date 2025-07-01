package io.groom.scubadive.shoppingmall.member.dto.response;

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
    private String phoneNumber;
    private String role;
    private String status;
    private String grade;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

}
