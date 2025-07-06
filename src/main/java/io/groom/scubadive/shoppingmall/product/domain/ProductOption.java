package io.groom.scubadive.shoppingmall.product.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ProductOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    private String color;

    @Column(unique = true, nullable = false)
    private String sku; // SKU 중복 방지

    @Column(nullable = false)
    private Long stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductOptionStatus status = ProductOptionStatus.ACTIVE; // 기본값 ACTIVE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionImage> productOptionImages = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public ProductOption(String color, String sku, Long stock, ProductOptionStatus status, Product product) {
        this.color = color;
        this.sku = sku;
        this.stock = stock;
        this.status = status != null ? status : ProductOptionStatus.ACTIVE;
        this.product = product;
    }

    public static ProductOption createProductOption(String color, String sku, Long stock, ProductOptionStatus status, Product product) {
        return ProductOption.builder()
                .color(color)
                .sku(sku)
                .stock(stock)
                .status(status)
                .product(product)
                .build();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateStock(Long stock) {
        this.stock = stock;
    }

    public void updateStatus(ProductOptionStatus status) {
        this.status = status;
    }

    public void addProductOptionImage(ProductOptionImage image) {
        if (!productOptionImages.contains(image)) {
            productOptionImages.add(image);
            image.setProductOption(this); // 단방향만 유지, 무한 루프 방지
        }
    }
}
