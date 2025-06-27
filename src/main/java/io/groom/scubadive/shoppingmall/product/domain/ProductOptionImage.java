package io.groom.scubadive.shoppingmall.product.domain;


import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private String imagePath;
    private String bucket;

    public void addProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    public String getImageUrl() {
        return "https://cdn.scubadive.com" + imagePath;
    }


    @Builder(access = AccessLevel.PROTECTED)
    public ProductOptionImage(ProductOption productOption, String imagePath, String bucket) {
        this.productOption = productOption;
        this.imagePath = imagePath;
        this.bucket = bucket;
    }


    public static ProductOptionImage createProductOptionImage(ProductOption productOption, String imagePath, String bucket) {
        return ProductOptionImage
                .builder()
                .productOption(productOption)
                .imagePath(imagePath)
                .bucket(bucket)
                .build();
    }
}
