package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.ProductOption;

public record ProductOptionResponse(
        Long id,
        String color,
        String sku,
        String status,
        Integer stock
) {
    public static ProductOptionResponse from(ProductOption productOption) {
        return new ProductOptionResponse(
                productOption.getId(),
                productOption.getColor(),
                productOption.getSku(),
                productOption.getStatus().name(),
                productOption.getStock().intValue()
        );
    }
}
