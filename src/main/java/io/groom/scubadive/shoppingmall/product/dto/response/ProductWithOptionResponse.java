package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 + 옵션 정보를 포함한 관리자용 응답 DTO")
public record ProductWithOptionResponse(

        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @Schema(description = "상품명", example = "스쿠버 장비 풀세트")
        String productName,

        @Schema(description = "카테고리 정보")
        CategoryResponse category,

        @Schema(description = "옵션 ID", example = "101")
        Long optionId,

        @Schema(description = "옵션 색상", example = "RED")
        String color,

        @Schema(description = "SKU (재고 단위)", example = "CHAIR-RED-001")
        String sku,

        @Schema(description = "상품 가격", example = "299000")
        Long price,

        @Schema(description = "재고 수량", example = "15")
        Integer stock,

        @Schema(description = "옵션 상태 (ACTIVE, SOLD_OUT)", example = "ACTIVE")
        String status

) {
    public static ProductWithOptionResponse from(Product product, ProductOption option) {
        return new ProductWithOptionResponse(
                product.getId(),
                product.getProductName(),
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
