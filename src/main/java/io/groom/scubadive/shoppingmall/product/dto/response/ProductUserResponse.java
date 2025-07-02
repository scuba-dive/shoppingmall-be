package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;

import java.math.BigDecimal;

public record ProductUserResponse(
        Long id,
        String name,
        Long price,
        BigDecimal rating,
        Long reviewCount,
        String thumbnailUrl,
        CategoryResponse category
        // Status는 대기 Product에는 status가 없음.
) {

    public static ProductUserResponse from(Product product) {
        String thumbnailUrl = product.getOptions().stream()
                .findFirst()
                .flatMap(option -> option.getProductOptionImages().stream().findFirst())
                .map(img -> "https://cdn.scubadive.com" + img.getImagePath())
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
