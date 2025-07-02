package io.groom.scubadive.shoppingmall.member.dto.response;

import io.groom.scubadive.shoppingmall.member.domain.enums.Grade;
import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "사용자 상태 변경 응답 DTO")
public class UserAdminStatusUpdateResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이메일", example = "admin@example.com")
    private String email;

    @Schema(description = "사용자 닉네임", example = "admin123")
    private String nickname;

    @Schema(description = "변경된 사용자 상태 (ACTIVE, DORMANT_AUTO, DORMANT_MANUAL)", example = "DORMANT_MANUAL")
    private UserStatus status;

    @Schema(description = "사용자 등급", example = "GOLD")
    private Grade grade;

    @Schema(description = "사용자 권한", example = "ADMIN")
    private Role role;

    @Schema(description = "마지막 로그인 일시", example = "2024-07-01T10:22:33")
    private LocalDateTime lastLoginAt;

    @Schema(description = "계정 생성 일시", example = "2024-01-15T08:00:00")
    private LocalDateTime createdAt;
}
