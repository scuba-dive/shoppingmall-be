package io.groom.scubadive.shoppingmall.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None") // CSRF 보호를 위해 SameSite=None 설정
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

    public static String getRefreshTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }



    public static void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None") // CSRF 보호를 위해 SameSite=None 설정
                .maxAge(0) // 즉시 만료
                .build();

        response.setHeader("Set-Cookie", expiredCookie.toString());
    }
}
