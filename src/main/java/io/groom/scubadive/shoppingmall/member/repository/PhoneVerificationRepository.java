package io.groom.scubadive.shoppingmall.member.repository;

import io.groom.scubadive.shoppingmall.member.domain.PhoneVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, String> {
    void deleteByPhoneNumber(String phoneNumber);
}
