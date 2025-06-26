package io.groom.scubadive.shoppingmall.memeber.repository;

import io.groom.scubadive.shoppingmall.memeber.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 중복 확인
    boolean existByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);

}
