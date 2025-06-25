package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
