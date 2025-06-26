package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;

public record ProductWithOptionResponse(
        Long productId,
        String productName,
        CategoryResponse category,
        Long optionId,
        String color,
        String sku,
        Long price,
        Integer stock,
        String status
) {

    public static ProductWithOptionResponse from(Product product, ProductOption option) {
        return new ProductWithOptionResponse(
                product.getId(),
                product.getName(),
                CategoryResponse.from(product.getCategory()),
                option.getId(),
                option.getColor(),
                option.getSku(),
                product.getPrice(),
                option.getStock().intValue(),
                option.getStatus().name()
        );
    }
}

