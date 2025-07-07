package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.PhoneVerification;
import io.groom.scubadive.shoppingmall.member.repository.PhoneVerificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhoneVerificationService {


    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    private final PhoneVerificationRepository repository;

    private DefaultMessageService messageService;


    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendVerificationCode(String toPhoneNumber) {
        String code = generateRandomCode();

        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(toPhoneNumber);
        message.setText("[회원가입 인증번호] " + code);

        try {
            messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.SMS_SEND_FAILED);
        }

        // 인증 정보 DB에 저장 (갱신 or 신규 저장)
        repository.save(PhoneVerification.builder()
                .phoneNumber(toPhoneNumber)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .verified(false)
                .build());
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void verifyCode(String phoneNumber, String inputCode) {
        PhoneVerification verification = repository.findById(phoneNumber)
                .orElseThrow(() -> new GlobalException(ErrorCode.VERIFICATION_NOT_FOUND));

        if (verification.isExpired()) {
            throw new GlobalException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!verification.getCode().equals(inputCode)) {
            throw new GlobalException(ErrorCode.VERIFICATION_CODE_INVALID);
        }

        verification.verify();
        repository.save(verification);
    }

    @Transactional
    public void deleteCode(String phoneNumber) {
        repository.deleteByPhoneNumber(phoneNumber);
    }
}
