package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.global.securirty.JwtTokenProvider;
import io.groom.scubadive.shoppingmall.member.domain.RefreshToken;
import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import io.groom.scubadive.shoppingmall.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String reissueAccessToken(String refreshToken) {
        log.info("[TOKEN SERVICE] 요청받은 refreshToken: {}", refreshToken);
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("[TOKEN] 유효하지 않은 RefreshToken: {}", refreshToken);
            throw new GlobalException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        log.info("[TOKEN SERVICE] 추출된 userId: {}", userId);

        // DB에 저장된 refresh token과 일치하는지 확인
        RefreshToken savedToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() ->
                    new GlobalException(ErrorCode.INVALID_REFRESH_TOKEN)
                );

        if (!savedToken.getToken().equals(refreshToken)) {
            log.warn("[TOKEN SERVICE] DB에 저장된 refreshToken과 일치하지 않음 - userId: {}", userId);
            throw new GlobalException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        log.info("[TOKEN SERVICE] refreshToken 일치 확인 - accessToken 재발급 시작");

        String roleStr = jwtTokenProvider.getRoleFromToken(refreshToken);
        Role role = Role.valueOf(roleStr);

        log.info("[TOKEN SERVICE] 재발급된 accessToken: {}", role);

        return jwtTokenProvider.createAccessToken(userId, role);
    }
}
