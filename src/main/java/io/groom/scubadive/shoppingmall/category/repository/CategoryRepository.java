package io.groom.scubadive.shoppingmall.category.repository;

import io.groom.scubadive.shoppingmall.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
