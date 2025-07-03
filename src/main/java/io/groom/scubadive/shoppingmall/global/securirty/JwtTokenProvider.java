package io.groom.scubadive.shoppingmall.global.securirty;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    // ✅ AccessToken: 사용자 정보 포함
    public String createAccessToken(Long userId, Enum<?> role, String username, String email) {
        return createToken(userId, role, jwtProperties.getAccessTokenExpiration(), username, email);
    }

    // ✅ RefreshToken: 사용자 정보 없이 생성
    public String createRefreshToken(Long userId) {
        return createToken(userId, null, jwtProperties.getRefreshTokenExpiration());
    }

    // ✅ AccessToken용 createToken (username, email 포함)
    private String createToken(Long userId, Enum<?> role, long expirySeconds, String username, String email) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        if (role != null) {
            claims.put("role", role.name());
        }

        claims.put("username", username);
        claims.put("email", email);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirySeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ RefreshToken용 오버로드 (username, email 없이)
    private String createToken(Long userId, Enum<?> role, long expirySeconds) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        if (role != null) {
            claims.put("role", role.name());
        }

        // username, email은 넣지 않음 (보안상 생략)

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirySeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }

    public long getRefreshTokenExpirySeconds() {
        return jwtProperties.getRefreshTokenExpiration();
    }
}
