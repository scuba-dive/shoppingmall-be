package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailUserResponse(
        Long id,
        String name,
        String description,
        Long price,
        CategoryResponse category,
        BigDecimal rating,
        Long reviewCount,
        List<ProductOptionWithImagesResponse> options
) {
    public static ProductDetailUserResponse from(Product product) {
        return new ProductDetailUserResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                CategoryResponse.from(product.getCategory()),
                product.getRating(),
                product.getReviewCount(),
                product.getOptions().stream().map(ProductOptionWithImagesResponse::from).toList()
        );
    }
}
