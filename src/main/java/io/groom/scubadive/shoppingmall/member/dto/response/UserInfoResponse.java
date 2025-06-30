package io.groom.scubadive.shoppingmall.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String role;
    private String status;
    private String grade;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
