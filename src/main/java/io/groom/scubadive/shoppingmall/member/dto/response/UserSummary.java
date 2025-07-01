package io.groom.scubadive.shoppingmall.member.dto.response;

import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.enums.Grade;
import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 로그인 성공 또는 사용자 조회 시 클라이언트에게 반환할 유저 요약 정보 DTO
 * 도메인 객체(User) 전체를 그대로 노출하지 않고 필요한 정보만 추려 제공합니다.
 */
@Getter
@Setter
public class UserSummary {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
    private Role role;
    private UserStatus status;
    private Grade grade;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
