package io.groom.scubadive.shoppingmall.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInfoResponse {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;
    private String grade;
    private String imagePath;
    private Long totalPaid;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
