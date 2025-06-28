package io.groom.scubadive.shoppingmall.global.securirty;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}