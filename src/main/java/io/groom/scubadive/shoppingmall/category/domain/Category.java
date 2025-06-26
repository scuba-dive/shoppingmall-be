package io.groom.scubadive.shoppingmall.category.domain;

import io.groom.scubadive.shoppingmall.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    public Category(String name) {
        this.name = name;
    }

    public static Category createCategory(String name) {
        return Category.builder()
                .name(name)
                .build();
    }
}
