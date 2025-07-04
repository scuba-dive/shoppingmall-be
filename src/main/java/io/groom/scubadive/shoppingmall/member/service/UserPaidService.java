package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.UserPaid;
import io.groom.scubadive.shoppingmall.member.repository.UserPaidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPaidService {
    private final UserPaidRepository userPaidRepository;

    @Transactional
    public void addPayment(Long userId, Long amount) {
        UserPaid userPaid = userPaidRepository.findByUserId(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_PAID_NOT_FOUND));
        userPaid.addPayment(amount); // += amount
    }
}