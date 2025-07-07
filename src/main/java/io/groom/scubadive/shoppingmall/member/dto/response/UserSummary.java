package io.groom.scubadive.shoppingmall.member.dto.response;

import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.enums.Grade;
import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDateTime;

/**
 * 로그인 성공 또는 사용자 조회 시 클라이언트에게 반환할 유저 요약 정보 DTO
 * 도메인 객체(User) 전체를 그대로 노출하지 않고 필요한 정보만 추려 제공합니다.
 */
@Getter
@Schema(description = "유저 요약 정보 DTO")
public class UserSummary {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 실명", example = "홍길동")
    private String username;

    @Schema(description = "닉네임", example = "gildong123")
    private String nickname;

    @Schema(description = "이메일", example = "gildong@example.com")
    private String email;

    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/images/profile.jpg")
    private String imagePath;

    @Schema(description = "권한", example = "USER")
    private Role role;

    @Schema(description = "상태", example = "ACTIVE")
    private UserStatus status;

    @Schema(description = "등급", example = "BRONZE")
    private Grade grade;

    @Schema(description = "마지막 로그인 시각", example = "2024-07-01T15:30:00")
    private LocalDateTime lastLoginAt;

    @Schema(description = "계정 생성일", example = "2024-01-10T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "계정 수정일", example = "2024-06-30T18:00:00")
    private LocalDateTime updatedAt;

    public UserSummary(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.imagePath =
                user.getUserImage().getImageUrl() != null ?
                        user.getUserImage().getImageUrl() :
                        "https://api.dicebear.com/9.x/notionists-neutral/svg?seed="+ user.getNickname();
        this.grade = user.getGrade();
        this.lastLoginAt = user.getLastLoginAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
