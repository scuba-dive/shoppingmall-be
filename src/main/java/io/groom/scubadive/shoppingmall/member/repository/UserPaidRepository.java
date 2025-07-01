package io.groom.scubadive.shoppingmall.member.repository;

import io.groom.scubadive.shoppingmall.member.domain.UserPaid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPaidRepository extends JpaRepository<UserPaid, Long> {


    Optional<Object> findByUserId(Long userId);
}
