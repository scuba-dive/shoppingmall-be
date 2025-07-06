package io.groom.scubadive.shoppingmall.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductCreateRequest {

    @Schema(description = "상품명", example = "데코1")
    private String productName;

    @Schema(description = "상품 설명", example = "감성 인테리어 소품 세트")
    private String description;

    @Schema(description = "상품 가격", example = "15000")
    private Long price;

    @Schema(description = "카테고리명(한글)", example = "소품")
    private String categoryName; // 한글명 ("의자", "책상" ...)


    private List<ProductOptionRequest> options;

    @Getter
    @Schema(description = "상품 옵션 요청")
    public static class ProductOptionRequest {

        @Schema(description = "색상", example = "파랑")
        private String color; // "파랑", "빨강", "검정"
    }
}
