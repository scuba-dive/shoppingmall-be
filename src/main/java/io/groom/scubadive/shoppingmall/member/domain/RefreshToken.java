package io.groom.scubadive.shoppingmall.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private Long userId;

    @Column(nullable = false)
    private String token;

    public RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
    public void updateToken(String token) {
        this.token = token;
    }
}
