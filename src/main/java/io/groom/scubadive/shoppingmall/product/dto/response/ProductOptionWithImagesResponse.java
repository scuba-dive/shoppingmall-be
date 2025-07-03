package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionImage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 옵션 및 이미지 정보")
public record ProductOptionWithImagesResponse(

        @Schema(description = "옵션 ID", example = "1")
        Long id,

        @Schema(description = "색상", example = "RED")
        String color,

        @Schema(description = "SKU (고유 식별자)", example = "DIVING-RED-001")
        String sku,

        @Schema(description = "상품 상태", example = "ACTIVE")
        String status,

        @Schema(description = "재고 수량", example = "5")
        Integer stock,

        @Schema(description = "이미지 URL 목록", example = "[\"https://my-shop-image-bucket.s3.ap-northeast-2.amazonaws.com/product/bed1-blue.webp\", \"https://my-shop-image-bucket.s3.ap-northeast-2.amazonaws.com/product/bed2-red.webp\"]")
        List<String> images

) {
    public static ProductOptionWithImagesResponse from(ProductOption productOption) {
        return new ProductOptionWithImagesResponse(
                productOption.getId(),
                productOption.getColor(),
                productOption.getSku(),
                productOption.getStatus().name(),
                productOption.getStock().intValue(),
                productOption.getProductOptionImages().stream()
                        .map(ProductOptionImage::getImageUrl)
                        .toList()
        );
    }
}
