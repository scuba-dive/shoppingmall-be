package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select count(po) from ProductOption po join po.product p where p.category.id = :categoryId")
    Long countOptionsByCategoryId(@Param("categoryId") Long categoryId);
}
