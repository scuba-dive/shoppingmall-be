package io.groom.scubadive.shoppingmall.member.service;

import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 사용자(User) 관련 비즈니스 로직을 담당하는 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // ================
    // 일반 사용자 기능
    // ================

    /**
     * 이메일(email)을 기준으로 사용자 조회
     * @param email
     * @return Optional<User> (사용자가 존재하지 않을 수 있음)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 닉네임(nickname) 중복 여부 확인
     * @param nickname
     * @return 존재하면 true, 존재하지 않으면 false
     */
    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 사용자 정보 저장
     * @param user
     * @return 저장된 User 객체
     */
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ================
    // 관리자 전용 기능 (UserAdminService 분리?)
    // ================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 상태 변경 (관리자 수동 변경 또는 시스템 자동 처리용)
     * @param userId
     * @param status
     */
    @Transactional
    public void changeUserStatus(Long userId, UserStatus status) {
        User user = getUserById(userId);
        user.changeStatus(status);
    }

    /**
     * 사용자 삭제 (휴면 전환 또는 실제 삭제 ?)
     * @param id
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.changeStatus(UserStatus.DORMANT_MANUAL);
    }


}
