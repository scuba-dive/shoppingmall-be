package io.groom.scubadive.shoppingmall.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "내 정보 조회 응답 DTO")
public class UserInfoResponse {

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

    @Schema(description = "사용자 권한", example = "USER")
    private String role;

    @Schema(description = "계정 상태", example = "active")
    private String status;

    @Schema(description = "등급", example = "BRONZE")
    private String grade;

    @Schema(description = "프로필 이미지 전체 URL", example = "https://bucket.s3.amazonaws.com/profile.jpg")
    private String imagePath;

    @Schema(description = "총 결제 금액", example = "150000")
    private Long totalPaid;

    @Schema(description = "마지막 로그인 시각", example = "2024-07-01T15:30:00")
    private LocalDateTime lastLoginAt;

    @Schema(description = "계정 생성일", example = "2024-01-10T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "계정 수정일", example = "2024-06-30T18:00:00")
    private LocalDateTime updatedAt;
}
