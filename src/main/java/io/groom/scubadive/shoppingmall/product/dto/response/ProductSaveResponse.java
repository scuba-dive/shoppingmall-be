package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "상품 등록 응답 DTO")
public record ProductSaveResponse(

        @Schema(description = "상품 ID", example = "1")
        Long id,

        @Schema(description = "상품명", example = "초호화 의자")
        String productName,

        @Schema(description = "상품 설명", example = "아무나 못 앉는 의자 입니다")
        String description,

        @Schema(description = "상품 가격", example = "129000")
        Long price,

        @Schema(description = "카테고리 정보")
        CategoryResponse category,

        @Schema(description = "상품 등록 일시", example = "2025-07-02T18:30:00")
        LocalDateTime createdAt,

        @Schema(description = "상품 옵션 목록")
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
