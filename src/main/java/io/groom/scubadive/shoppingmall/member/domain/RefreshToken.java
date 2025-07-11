package io.groom.scubadive.shoppingmall.member.domain;

import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public RefreshToken(Long userId, String token, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }
    public void updateToken(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }
}
