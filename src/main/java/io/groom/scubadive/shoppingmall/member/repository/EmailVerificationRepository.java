package io.groom.scubadive.shoppingmall.member.repository;

import io.groom.scubadive.shoppingmall.member.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByCode(String code);
}