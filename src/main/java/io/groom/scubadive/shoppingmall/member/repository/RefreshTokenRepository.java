package io.groom.scubadive.shoppingmall.member.repository;

import io.groom.scubadive.shoppingmall.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


}
