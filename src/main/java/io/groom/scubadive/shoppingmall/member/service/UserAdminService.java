package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.groom.scubadive.shoppingmall.member.dto.response.UserAdminStatusUpdateResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.UserAdminResponse;
import io.groom.scubadive.shoppingmall.member.repository.UserPaidRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final UserPaidRepository userPaidRepository;

    public Page<UserAdminResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> UserAdminResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole().name())
                        .status(user.getStatus().name())
                        .grade(user.getGrade().name())
                        .totalPaid(userPaidRepository.findByUserId(user.getId()).getAmount())
                        .createdAt(user.getCreatedAt())
                        .lastLoginAt(user.getLastLoginAt())
                        .build());
    }

    @Transactional
    public UserAdminStatusUpdateResponse updateStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        UserStatus current = user.getStatus();

        switch (current) {
            case ACTIVE, DORMANT_AUTO -> user.changeStatus(UserStatus.DORMANT_MANUAL);

            case DORMANT_MANUAL -> {
                if (user.isInactiveOverThreeMonths()) {
                    user.changeStatus(UserStatus.DORMANT_AUTO);
                } else {
                    user.changeStatus(UserStatus.ACTIVE);
                }
            }
        }

        return UserAdminStatusUpdateResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .status(user.getStatus())
                .grade(user.getGrade())
                .role(user.getRole())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
