package io.groom.scubadive.shoppingmall.global.securirty;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

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

    public String createAccessToken(Long userId, Enum<?> role) {
        return createToken(userId, role, jwtProperties.getAccessTokenExpiration());
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, null,  jwtProperties.getRefreshTokenExpiration());
    }

    private String createToken(Long userId, Enum<?> role, long expirySeconds) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        if (role != null) {
            claims.put("role", role.name());
        }

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
}