package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "사용자용 상품 상세 응답")
public record ProductDetailUserResponse(

        @Schema(description = "상품 ID", example = "1")
        Long id,

        @Schema(description = "상품 이름", example = "고오급 의자")
        String productName,

        @Schema(description = "상품 설명", example = "고급 소재로 제작된 의자입니다.")
        String description,

        @Schema(description = "상품 가격", example = "159000")
        Long price,

        @Schema(description = "카테고리 정보")
        CategoryResponse category,

        @Schema(description = "상품 평점", example = "4.5")
        BigDecimal rating,

        @Schema(description = "리뷰 수", example = "128")
        Long reviewCount,

        @Schema(description = "상품 옵션 목록 (이미지 포함)")
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
