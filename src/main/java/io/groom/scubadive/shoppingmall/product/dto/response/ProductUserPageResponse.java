package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "사용자 상품 페이지 응답 DTO")
public record ProductUserPageResponse(

        @Schema(description = "전체 상품 수", example = "120")
        long total,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지당 항목 수", example = "8")
        int size,

        @Schema(description = "상품 목록")
        List<ProductUserResponse> products

) {
    public static ProductUserPageResponse from(Page<Product> productPage) {
        List<ProductUserResponse> results = productPage.getContent().stream()
                .map(ProductUserResponse::from)
                .toList();

        return new ProductUserPageResponse(
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize(),
                results
        );
    }
}
