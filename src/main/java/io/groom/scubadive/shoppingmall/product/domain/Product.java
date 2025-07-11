package io.groom.scubadive.shoppingmall.product.domain;


import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String productName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private Long price;

    private Long reviewCount;

    @Column(precision = 10, scale = 2)
    private BigDecimal rating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder(access = AccessLevel.PRIVATE)
    public Product(String productName, String description, Long price, Long reviewCount, BigDecimal rating, Category category) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.category = category;
    }

    public static Product createProduct(String productName, String description, Long price, Long reviewCount, BigDecimal rating, Category category) {
        return Product.builder()
                .productName(productName)
                .description(description)
                .price(price)
                .reviewCount(reviewCount)
                .rating(rating)
                .category(category)
                .build();
    }

    public void addOption(ProductOption productOption) {
        productOption.setProduct(this);
        this.options.add(productOption);
    }

    public void addOptions(List<ProductOption> productOptions) {
        productOptions.forEach(this::addOption);
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }
}
