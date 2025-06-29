package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.global.securirty.JwtTokenProvider;
import io.groom.scubadive.shoppingmall.member.domain.RefreshToken;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.UserPaid;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.groom.scubadive.shoppingmall.member.dto.request.SignInRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.SignInResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.UserResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.UserSummary;
import io.groom.scubadive.shoppingmall.member.repository.RefreshTokenRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserPaidRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.member.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPaidRepository userPaidRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserResponse signUp(SignUpRequest dto) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new GlobalException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
        }

        // 닉네임 자동 생성 및 중복 방지
        String nickname = generateUniqueNickname();
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // User 엔티티 생성 (기본값 자동 설정 포함)
        User user = new User(
                dto.getUsername(),
                nickname,
                dto.getEmail(),
                encodedPassword,
                dto.getPhoneNumber(),
                dto.getAddress()
        );

        userRepository.save(user);

        // 초기 UserPaid 엔티티 생성
        UserPaid userPaid = new UserPaid(user);
        userPaidRepository.save(userPaid);

        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    private String generateUniqueNickname() {
        String nickname = NicknameGenerator.generate();
        while (userRepository.existsByNickname(nickname)) {
            nickname = NicknameGenerator.generateWithNumberSuffix();
        }
        return nickname;
    }

    @Transactional
    public SignInResponse login(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("[LOGIN_FAIL] 존재하지 않는 이메일: {}", request.getEmail());
                    return new GlobalException(ErrorCode.WRONG_PASSWORD); // 보안상 통일된 메시지
                });

        // 수동 휴면(소프트 삭제) 상태 로그인 차단
        if (user.getStatus() == UserStatus.DORMANT_MANUAL) {
            log.warn("[LOGIN_FAIL] 수동 휴면 상태 사용자 로그인 시도 - userId: {}", user.getId());
            throw new GlobalException(ErrorCode.MEMBER_DELETED);  // 404: 로그인할 수 없는 사용자입니다.
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("[LOGIN_FAIL] 비밀번호 불일치 - userId: {}", user.getId());
            throw new GlobalException(ErrorCode.WRONG_PASSWORD);  // 401: 아이디 혹은 비밀번호가 틀렸습니다.
        }
        user.updateLastLoginAt();

        // 자동 휴면 상태일 경우 로그인 시 ACTIVE로 복구
        if (user.getStatus() == UserStatus.DORMANT_AUTO) {
            log.info("[LOGIN_RECOVERY] 자동 휴면 상태에서 ACTIVE로 전환 - userId: {}", user.getId());
            user.updateStatus(UserStatus.ACTIVE);
        }

        // JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenExpirySeconds());

        // RefreshToken 저장 또는 갱신
        refreshTokenRepository.findById(user.getId())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, expiresAt),
                        () -> refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken, expiresAt))
                );

        return new SignInResponse(
                accessToken,
                refreshToken,
                new UserSummary(user)
        );
    }
}
