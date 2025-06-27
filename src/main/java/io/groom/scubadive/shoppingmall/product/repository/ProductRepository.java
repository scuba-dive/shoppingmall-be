package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("select count(po) from ProductOption po join po.product p where p.category.id = :categoryId")
    Long countOptionsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("select p from Product p join fetch p.options where p.id = :productId")
    Optional<Product> findWithOptionsById(Long productId);

}
