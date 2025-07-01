package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.member.dto.response.UserAdminResponse;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

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
                        .createdAt(user.getCreatedAt())
                        .lastLoginAt(user.getLastLoginAt())
                        .build());
    }

}
