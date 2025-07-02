package io.groom.scubadive.shoppingmall.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record ProductSaveRequest(

        @Schema(description = "상품 이름", example = "머찐 의자")
        @NotBlank
        String productName,

        @Schema(description = "상품 설명", example = "이 의자는 아무튼 좋아요")
        @NotBlank
        String description,

        @Schema(description = "상품 가격", example = "199000")
        @NotNull
        @PositiveOrZero
        Long price,

        @Schema(description = "카테고리 ID", example = "1")
        @NotNull
        Long categoryId,

        @Schema(description = "상품 옵션 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty
        List<ProductOptionRequest> options

) {}
