package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "상품 수정 응답 DTO")
public record ProductUpdateResponse(

        @Schema(description = "상품 ID", example = "1")
        Long id,

        @Schema(description = "상품명", example = "너무 편한 의자")
        String productName,

        @Schema(description = "상품 설명", example = "2025년 신상 의자입니다.")
        String description,

        @Schema(description = "상품 가격", example = "99000")
        Long price,

        @Schema(description = "카테고리 정보")
        CategoryResponse category,

        @Schema(description = "상품 수정 일시", example = "2025-07-02T22:10:00")
        LocalDateTime updatedAt

) {
    public static ProductUpdateResponse from(Product product) {
        return new ProductUpdateResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                CategoryResponse.from(product.getCategory()),
                product.getUpdatedAt()
        );
    }
}
