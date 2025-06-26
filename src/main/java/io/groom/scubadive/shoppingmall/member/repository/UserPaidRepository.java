package io.groom.scubadive.shoppingmall.memeber.repository;

import io.groom.scubadive.shoppingmall.memeber.domain.UserPaid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPaidRepository extends JpaRepository<UserPaid, Long> {
    boolean existsByUserId(Long userId);
    UserPaid findByUserId(Long userId);
}
