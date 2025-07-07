package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.EmailVerification;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.repository.EmailVerificationRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    // 이메일 인증 코드를 생성하고 저장하는 메서드
    public String createVerificationEntry(User user) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

        EmailVerification verification = EmailVerification.builder()
                .email(user.getEmail())
                .code(code)
                .expiresAt(expiresAt)
                .build();

        emailVerificationRepository.save(verification);

        return code;
    }

    // 이메일 인증 링크 클릭 시 인증 코드를 검증하는 메서드
    public void verifyEmailCode(String code) {
        EmailVerification verification = emailVerificationRepository.findByCode(code)
                .orElseThrow(() -> new GlobalException(ErrorCode.VERIFICATION_CODE_INVALID));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GlobalException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        User user = userRepository.findByEmail(verification.getEmail())
                .orElseThrow(() -> new GlobalException(ErrorCode.EMAIL_NOT_FOUND));

        if (user.isEmailVerified()) {
            throw new GlobalException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        user.setEmailVerified(true);
        userRepository.save(user);
    }
}
