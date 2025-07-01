package io.groom.scubadive.shoppingmall.cart.repository;

import io.groom.scubadive.shoppingmall.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}