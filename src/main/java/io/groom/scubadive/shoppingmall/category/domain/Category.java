package io.groom.scubadive.shoppingmall.category.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;
    private String koreanName;

    @Builder(access = AccessLevel.PRIVATE)
    public Category(String categoryName, String koreanName) {
        this.categoryName = categoryName;
        this.koreanName = koreanName;
    }

    public static Category createCategory(String categoryName,String koreanName) {
        return Category.builder()
                .categoryName(categoryName)
                .koreanName(koreanName)
                .build();
    }
}
