package io.groom.scubadive.shoppingmall.product.domain;


import io.groom.scubadive.shoppingmall.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;


    private Long price;

    private Long reviewCount;

    @Column(precision = 10, scale = 2)
    private BigDecimal rating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Product(String name, String description, Long price, Long reviewCount, BigDecimal rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

    public static Product createProduct(String name, String description, Long price, Long reviewCount, BigDecimal rating) {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .reviewCount(reviewCount)
                .rating(rating)
                .build();
    }
}
