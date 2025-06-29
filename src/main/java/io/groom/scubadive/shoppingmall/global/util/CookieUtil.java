package io.groom.scubadive.shoppingmall.global.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}
