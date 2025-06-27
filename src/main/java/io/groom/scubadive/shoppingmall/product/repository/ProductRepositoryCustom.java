package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductOption> findProductOptionPageable(Pageable pageable);

    Page<Product> findProductsPageable(Pageable pageable);
}
