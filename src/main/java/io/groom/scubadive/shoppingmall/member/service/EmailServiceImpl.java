package io.groom.scubadive.shoppingmall.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private static final String VERIFY_LINK_BASE_URL = "http://3.39.233.161:8080/api/users/email/verify?code=";

    @Override
    public void sendVerificationEmail(String toEmail, String code) {
        String subject = "이메일 인증을 완료해주세요";
        String verificationLink = VERIFY_LINK_BASE_URL + code;
        String body = """
                안녕하세요!
                
                DeepDive Studio 회원님,

                회원가입을 완료하시려면 아래 링크를 클릭하여 이메일 인증을 완료해주세요:

                %s

                (이 링크는 일정 시간 후 만료됩니다.)
                """.formatted(verificationLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
