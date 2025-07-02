package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "사용자 상품 리스트 응답 DTO")
public record ProductUserResponse(

        @Schema(description = "상품 ID", example = "1")
        Long id,

        @Schema(description = "상품명", example = "생각하는 의자")
        String productName,

        @Schema(description = "상품 가격", example = "129000")
        Long price,

        @Schema(description = "상품 평점", example = "4.5")
        BigDecimal rating,

        @Schema(description = "상품 리뷰 수", example = "38")
        Long reviewCount,

        @Schema(description = "썸네일 이미지 URL", example = "https://my-shop-image-bucket.s3.ap-northeast-2.amazonaws.com/images/thumbnail.jpg")
        String thumbnailUrl,

        @Schema(description = "카테고리 정보")
        CategoryResponse category

) {
    public static ProductUserResponse from(Product product) {
        String thumbnailUrl = product.getOptions().stream()
                .findFirst()
                .flatMap(option -> option.getProductOptionImages().stream().findFirst())
                .map(img -> "https://my-shop-image-bucket.s3.ap-northeast-2.amazonaws.com" + img.getImagePath())
                .orElse(null);

        return new ProductUserResponse(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getRating(),
                product.getReviewCount(),
                thumbnailUrl,
                CategoryResponse.from(product.getCategory())
        );
    }
}
