package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.global.securirty.JwtTokenProvider;
import io.groom.scubadive.shoppingmall.global.util.CookieUtil;
import io.groom.scubadive.shoppingmall.member.domain.RefreshToken;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.UserPaid;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.groom.scubadive.shoppingmall.member.dto.request.SignInRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.UpdateUserRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.*;
import io.groom.scubadive.shoppingmall.member.repository.RefreshTokenRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserPaidRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.member.util.NicknameGenerator;
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * 회원가입 처리 메서드
     * 1. 요청 검증 → 2. 닉네임 생성 → 3. 사용자 저장 → 4. 응답 반환
     */
    @Transactional
    public UserResponse signUp(SignUpRequest dto) {
        validateSignUpRequest(dto);

        String nickname = generateUniqueNickname();
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = createUserFromRequest(dto, nickname, encodedPassword);

        userRepository.save(user);
        userPaidRepository.save(new UserPaid(user));

        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    // 이메일 중복 및 비밀번호 확인 검증
    private void validateSignUpRequest(SignUpRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new GlobalException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
        }
    }

    // 요청으로부터 사용자 엔티티 생성
    private User createUserFromRequest(SignUpRequest dto, String nickname, String encodedPassword) {
        return new User(
                dto.getUsername(), nickname, dto.getEmail(),
                encodedPassword, dto.getPhoneNumber(), dto.getAddress(), null
        );
    }

    // 닉네임 중복 방지 자동 생성
    private String generateUniqueNickname() {
        String nickname = NicknameGenerator.generate();
        while (userRepository.existsByNickname(nickname)) {
            nickname = NicknameGenerator.generateWithNumberSuffix();
        }
        return nickname;
    }

    /**
     * 로그인 처리 메서드
     * 1. 사용자 조회 → 2. 상태 및 비밀번호 검증 → 3. 토큰 발급 및 저장 → 4. 응답 반환
     */
    @Transactional
    public LoginResult login(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("[LOGIN_FAIL] 존재하지 않는 이메일: {}", request.getEmail());
                    return new GlobalException(ErrorCode.WRONG_PASSWORD);
                });

        validateLoginUser(user, request.getPassword());

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenExpirySeconds());



        // RefreshToken 저장 또는 갱신
        refreshTokenRepository.findById(user.getId())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, expiresAt),
                        () -> refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken, expiresAt))
                );

        return new LoginResult(accessToken, refreshToken, new UserSummary(user));
    }

    // 로그인 시 사용자 상태 및 비밀번호 검증 + 로그인 기록 업데이트
    private void validateLoginUser(User user, String rawPassword) {
        if (user.getStatus() == UserStatus.DORMANT_MANUAL) {
            log.warn("[LOGIN_FAIL] 수동 휴면 상태 사용자 로그인 시도 - userId: {}", user.getId());
            throw new GlobalException(ErrorCode.MEMBER_DELETED);
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.warn("[LOGIN_FAIL] 비밀번호 불일치 - userId: {}", user.getId());
            throw new GlobalException(ErrorCode.WRONG_PASSWORD);
        }

        user.updateLastLoginAt();

        if (user.getStatus() == UserStatus.DORMANT_AUTO) {
            log.info("[LOGIN_RECOVERY] 자동 휴면 상태에서 ACTIVE로 전환 - userId: {}", user.getId());
            user.updateStatus(UserStatus.ACTIVE);
        }
    }

    /**
     * 내 정보 조회 - 로그인된 사용자 정보 반환
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        UserPaid userPaid = (UserPaid) userPaidRepository.findByUserId(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_PAID_NOT_FOUND));

        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .status(user.getStatus().name().toLowerCase())
                .grade(user.getGrade().name())
                .imagePath(user.getUserImage() != null ? user.getUserImage().getImagePath() : null)
                .totalPaid(userPaid.getAmount())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 내 정보 수정 - 비밀번호/닉네임/전화번호 변경
     * 변경된 항목이 없으면 예외 발생
     */
    @Transactional
    public UpdateUserResponseWrapper updateMyInfo(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        boolean isUpdated = false;
        boolean passwordChanged = false;

        // 비밀번호 변경 처리
        if (shouldChangePassword(request)) {
            validatePasswordChange(user, request);
            user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
            refreshTokenRepository.deleteById(userId);
            isUpdated = true;
            passwordChanged = true;
        }

        // 닉네임 변경 처리
        if (shouldChangeNickname(user, request)) {
            user.updateNickname(request.getNickname());
            isUpdated = true;
        }

        // 전화번호 변경 처리
        if (shouldChangePhoneNumber(user, request)) {
            user.updatePhoneNumber(request.getPhoneNumber());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new GlobalException(ErrorCode.NO_CHANGES_REQUESTED);
        }

        return UpdateUserResponseWrapper.builder()
                .user(buildUserInfoResponse(user))
                .loggedOut(passwordChanged)
                .build();
    }

    // 조건: 비밀번호 변경 필요 여부
    private boolean shouldChangePassword(UpdateUserRequest request) {
        return request.getNewPassword() != null && !request.getNewPassword().isBlank();
    }

    // 비밀번호 변경 유효성 검사
    private void validatePasswordChange(User user, UpdateUserRequest request) {
        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new GlobalException(ErrorCode.PASSWORD_REQUIRED);
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new GlobalException(ErrorCode.WRONG_PASSWORD);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new GlobalException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }
    }

    // 조건: 닉네임 변경 필요 여부
    private boolean shouldChangeNickname(User user, UpdateUserRequest request) {
        return request.getNickname() != null && !request.getNickname().equals(user.getNickname())
                && !userRepository.existsByNicknameAndIdNot(request.getNickname(), user.getId());
    }

    // 조건: 전화번호 변경 필요 여부
    private boolean shouldChangePhoneNumber(User user, UpdateUserRequest request) {
        return request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber());
    }

    // 사용자 정보 DTO 빌더
    private UserInfoResponse buildUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .status(user.getStatus().name().toLowerCase())
                .grade(user.getGrade().name())
                .imagePath(user.getUserImage() != null ? user.getUserImage().getFullImageUrl() : null)
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 로그아웃 처리 - RefreshToken 삭제 및 쿠키 제거
     */
    @Transactional
    public void logout(Long userId, HttpServletResponse response) {
        if (userId == null) {
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_REQUIRED);
        }
        refreshTokenRepository.deleteById(userId);
        CookieUtil.deleteRefreshTokenCookie(response);
    }
}

