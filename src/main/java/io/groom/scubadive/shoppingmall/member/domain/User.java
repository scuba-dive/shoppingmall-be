package io.groom.scubadive.shoppingmall.member.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import io.groom.scubadive.shoppingmall.member.domain.enums.Grade;
import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uq_users_email", columnNames = "email")
})
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String username; // 사용자 실명

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임 (고유)

    @Column(nullable = false, unique = true)
    private String email; // 이메일 (고유)

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    private String phoneNumber; // 전화번호

    private String address; // 주소

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status; // 사용자 상태 (ACTIVE, 휴면 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade; // 등급 (BRONZE, SILVER, GOLD 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 권한 (USER, ADMIN 등)

    private LocalDateTime lastLoginAt; // 마지막 로그인 시각

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserImage userImage; // 프로필 이미지

    /**
     * 사용자 생성자 (가입 시 사용)
     * 기본 상태: ACTIVE / 기본 등급: BRONZE / 기본 권한: USER
     */
    public User(String username, String nickname, String email, String password,
                String phoneNumber, String address, UserImage userImage) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = UserStatus.ACTIVE;
        this.grade = Grade.BRONZE;
        this.role = Role.USER;
        this.userImage = userImage;
        this.lastLoginAt = LocalDateTime.now(); // 최초 가입 시 현재 시각 설정
    }

    // 마지막 로그인 시간 업데이트
    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    // 사용자 상태 변경
    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 전화번호 변경
    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // 비밀번호 변경 (암호화된 값)
    public void updatePassword(String newEncodedPassword) {
        this.password = newEncodedPassword;
    }

    // 3개월 이상 로그인 기록이 없는지 확인
    public boolean isInactiveOverThreeMonths() {
        if (lastLoginAt == null) return true;
        return lastLoginAt.isBefore(LocalDateTime.now().minusMonths(3));
    }

    // 상태 직접 변경 (중복 메서드지만 의미적으로 분리한 듯함)
    public void changeStatus(UserStatus userStatus) {
        this.status = userStatus;
    }
}

