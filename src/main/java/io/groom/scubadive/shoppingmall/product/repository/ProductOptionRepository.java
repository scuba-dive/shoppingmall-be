package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
