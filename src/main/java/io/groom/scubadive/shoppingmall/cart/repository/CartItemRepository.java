package io.groom.scubadive.shoppingmall.cart.repository;

import io.groom.scubadive.shoppingmall.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductOptionId(Long cartId, Long productOptionId);

}