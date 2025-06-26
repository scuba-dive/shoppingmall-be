package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;

import java.time.LocalDateTime;

public record ProductUpdateResponse(
        Long id,
        String name,
        String description,
        Long price,
        CategoryResponse category,
        LocalDateTime updatedAt
) {
    public static ProductUpdateResponse from(Product product) {
        return new ProductUpdateResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                CategoryResponse.from(product.getCategory()),
                product.getUpdatedAt()
        );
    }

}
