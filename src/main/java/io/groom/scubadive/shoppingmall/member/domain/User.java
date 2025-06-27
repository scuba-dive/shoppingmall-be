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
@Table(name = "users",  uniqueConstraints = {
        @UniqueConstraint(name = "uq_users_email", columnNames = "email")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastLoginAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_image_id")
    private UserImage userImage;


    public User(String username, String nickname, String email, String password, String phoneNumber, String address) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;

        this.status = UserStatus.ACTIVE;
        this.grade = Grade.BRONZE;
        this.role = Role.USER;
    }

    public void updateLastLoginTime() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    public void setUserImage(UserImage userImage) {
        this.userImage = userImage;
    }
}
