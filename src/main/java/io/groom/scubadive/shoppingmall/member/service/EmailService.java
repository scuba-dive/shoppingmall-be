package io.groom.scubadive.shoppingmall.member.service;

public interface EmailService {

    void sendVerificationEmail(String toEmail, String code);

}
