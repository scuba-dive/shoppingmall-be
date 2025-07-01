package io.groom.scubadive.shoppingmall.cart.repository;

import io.groom.scubadive.shoppingmall.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}