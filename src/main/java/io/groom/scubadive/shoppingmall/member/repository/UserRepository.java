package io.groom.scubadive.shoppingmall.member.repository;

import io.groom.scubadive.shoppingmall.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByNicknameAndIdNot(String nickname, Long id);

    Optional<User> findByEmail(String email);


}
