package io.groom.scubadive.shoppingmall.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "관리자용 사용자 정보 응답 DTO")
public class UserAdminResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String username;

    @Schema(description = "사용자 닉네임", example = "gildong123")
    private String nickname;

    @Schema(description = "사용자 이메일", example = "gildong@example.com")
    private String email;

    @Schema(description = "사용자 전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "역할 (USER 또는 ADMIN)", example = "USER")
    private String role;

    @Schema(description = "상태 (ACTIVE, DORMANT_AUTO, DORMANT_MANUAL)", example = "ACTIVE")
    private String status;

    @Schema(description = "등급 (BRONZE, SILVER, GOLD, VIP)", example = "SILVER")
    private String grade;

    @Schema(description = "총 결제 금액", example = "120000")
    private Long totalPaid;

    @Schema(description = "계정 생성일시", example = "2024-06-01T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 로그인 일시", example = "2024-06-15T09:12:30")
    private LocalDateTime lastLoginAt;
}
