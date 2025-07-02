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

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String bucket;

    public String getImageUrl() {
        // bucket을 포함한 URL로 변경하거나, bucket 필드 제거 고려
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com" + imagePath;
        // bucket = my-shop-image-bucket
        // imagePath = /product/bed1-blue.webp
    }

    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    @Builder(access = AccessLevel.PROTECTED)
    public ProductOptionImage(ProductOption productOption, String imagePath, String bucket) {
        this.productOption = productOption;
        this.imagePath = imagePath;
        this.bucket = bucket;
    }

    public static ProductOptionImage createProductOptionImage(ProductOption productOption, String imagePath, String bucket) {
        return ProductOptionImage.builder()
                .productOption(productOption)
                .imagePath(imagePath)
                .bucket(bucket)
                .build();
    }
}

