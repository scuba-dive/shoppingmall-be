package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<ProductOption> findProductOptionPageable(Pageable pageable);

    Page<Product> findProductsPageable(Pageable pageable);

    Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);
}
