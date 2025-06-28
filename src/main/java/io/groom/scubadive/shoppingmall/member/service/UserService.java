package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.UserPaid;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.UserResponse;
import io.groom.scubadive.shoppingmall.member.repository.UserPaidRepository;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.member.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPaidRepository userPaidRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse signUp(SignUpRequest dto) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 닉네임 자동 생성 및 중복 방지
        String nickname = generateUniqueNickname();

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // User 엔티티 생성 (기본값 자동 설정 포함)
        User user = new User(
                dto.getUsername(),
                nickname,
                dto.getEmail(),
                encodedPassword,
                dto.getPhoneNumber(),
                dto.getAddress()
        );

        userRepository.save(user);

        // 초기 UserPaid 엔티티 생성
        UserPaid userPaid = new UserPaid(user);
        userPaidRepository.save(userPaid);

        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    private String generateUniqueNickname() {
        String nickname = NicknameGenerator.generate();
        while (userRepository.existsByNickname(nickname)) {
            nickname = NicknameGenerator.generateWithNumberSuffix();
        }
        return nickname;
    }
}
