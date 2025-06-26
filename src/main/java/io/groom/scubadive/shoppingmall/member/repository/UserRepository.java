package io.groom.scubadive.shoppingmall.member.repository;

import io.groom.scubadive.shoppingmall.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자(User) 엔티티에 대한 데이터 접근 저장소
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일 중복 여부 확인
     * @param email
     * @return 이미 존재하면 true
     */
    boolean existsByEmail(String email);

    /**
     * 이메일로 사용자 조회
     * @param email
     * @return 해당 이메일의 사용자 (없을 경우 Optional.empty)
     */
    Optional<User> findByEmail(String email);

    /**
     * 닉네임 중복 여부 확인
     * @param nickname
     * @return 이미 존재하면 true
     */
    boolean existsByNickname(String nickname);

}
