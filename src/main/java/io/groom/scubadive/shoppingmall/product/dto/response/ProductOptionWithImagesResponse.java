package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionImage;

import java.util.List;

public record ProductOptionWithImagesResponse(
        Long id,
        String color,
        String sku,
        String status,
        Integer stock,
        List<String> images
) {
    public static ProductOptionWithImagesResponse from(ProductOption productOption) {
        return new ProductOptionWithImagesResponse(
                productOption.getId(),
                productOption.getColor(),
                productOption.getSku(),
                productOption.getStatus().name(),
                productOption.getStock().intValue(),
                productOption.getProductOptionImages().stream().map(ProductOptionImage::getImageUrl).toList()
        );
    }
}
