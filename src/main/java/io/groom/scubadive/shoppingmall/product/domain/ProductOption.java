package io.groom.scubadive.shoppingmall.product.domain;


import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    private  String color;
    private String sku;
    private Long stock;

    @Enumerated(EnumType.STRING)
    private ProductOptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productOption")
    private List<ProductOptionImage> productOptionImages = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public ProductOption(String color, String sku, Long stock, ProductOptionStatus status, Product product) {
        this.color = color;
        this.sku = sku;
        this.stock = stock;
        this.status = status;
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

    public void addProductOptionImage(ProductOptionImage productOptionImage) {
        this.productOptionImages.add(productOptionImage);
        productOptionImage.addProductOption(this);
    }


}
