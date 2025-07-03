package io.groom.scubadive.shoppingmall.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "관리자용 상품 + 옵션 페이지 응답 DTO")
public record ProductWithOptionPageResponse(

        @Schema(description = "전체 상품 수", example = "124")
        long total,

        @Schema(description = "전체 페이지 수", example = "13")
        int totalPages,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지당 항목 수", example = "10")
        int size,

        @Schema(description = "상품 + 옵션 응답 목록")
        List<ProductWithOptionResponse> products

) {
    public static ProductWithOptionPageResponse from(Page<ProductOption> optionPage) {
        List<ProductWithOptionResponse> results = optionPage.getContent().stream()
                .map(option -> ProductWithOptionResponse.from(
                        option.getProduct(),
                        option
                ))
                .toList();

        return new ProductWithOptionPageResponse(
                optionPage.getTotalElements(),
                optionPage.getNumber(),
                optionPage.getSize(),
                optionPage.getTotalPages(),
                results
        );
    }
}
