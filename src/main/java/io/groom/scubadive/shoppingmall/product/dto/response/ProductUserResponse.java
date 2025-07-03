package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionImage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        List<ProductOption> options = product.getOptions();

        String thumbnailUrl = null;
        if (!options.isEmpty()) {
            // 옵션 중 랜덤 선택
            ProductOption randomOption = options.get(ThreadLocalRandom.current().nextInt(options.size()));
            List<ProductOptionImage> images = randomOption.getProductOptionImages();

            if (!images.isEmpty()) {
                // 해당 옵션의 이미지 중 랜덤 선택
                ProductOptionImage randomImage = images.get(ThreadLocalRandom.current().nextInt(images.size()));
                thumbnailUrl = "https://my-shop-image-bucket.s3.ap-northeast-2.amazonaws.com" + randomImage.getImagePath();
            }
        }

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
