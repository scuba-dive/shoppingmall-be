package io.groom.scubadive.shoppingmall.category.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "카테고리별 상품 페이지 응답 DTO")
public record CategoryProductUserPageResponse(

        @Schema(description = "전체 상품 수", example = "45")
        long total,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지 당 항목 수", example = "8")
        int size,

        @Schema(description = "상품 리스트")
        List<ProductUserResponse> products
) {

    public static CategoryProductUserPageResponse from(Page<Product> productPage) {
        List<ProductUserResponse> results = productPage.getContent().stream()
                .map(ProductUserResponse::from)
                .toList();

        return new CategoryProductUserPageResponse(
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize(),
                results
        );
    }
}
