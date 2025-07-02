package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;

import java.time.LocalDateTime;
import java.util.List;

public record ProductSaveResponse(
    Long id,
    String productName,
    String description,
    Long price,
    CategoryResponse category,
    LocalDateTime createdAt,
    List<ProductOptionResponse> options
) {
    public static ProductSaveResponse from(Product product) {
        return new ProductSaveResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                CategoryResponse.from(product.getCategory()),
                product.getCreatedAt(),
                product.getOptions().stream()
                        .map(ProductOptionResponse::from)
                        .toList()
        );
    }
}
