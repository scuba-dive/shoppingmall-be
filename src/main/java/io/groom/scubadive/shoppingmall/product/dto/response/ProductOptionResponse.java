package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 옵션 정보")
public record ProductOptionResponse(

        @Schema(description = "옵션 ID", example = "1")
        Long id,

        @Schema(description = "색상", example = "RED")
        String color,

        @Schema(description = "SKU (고유 식별자)", example = "CHAIR-RED-001")
        String sku,

        @Schema(description = "상품 상태", example = "ACTIVE")
        String status,

        @Schema(description = "재고 수량", example = "10")
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
