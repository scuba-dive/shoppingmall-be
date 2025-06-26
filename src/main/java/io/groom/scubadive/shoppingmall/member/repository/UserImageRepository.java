package io.groom.scubadive.shoppingmall.memeber.repository;

import io.groom.scubadive.shoppingmall.memeber.domain.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    boolean existsByUserId(Long userId);
    UserImage findByUserId(Long userId);
}
