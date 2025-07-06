package io.groom.scubadive.shoppingmall.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "toss")
public class TossPaymentConfig {
    private String testSecretKey;
    private String successUrl;
    private String failUrl;
}
