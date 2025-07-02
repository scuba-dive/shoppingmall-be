package io.groom.scubadive.shoppingmall.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductOptionRequest(

        @Schema(description = "옵션 색상 (예: 빨간색, 파란색)", example = "빨간색")
        @NotBlank
        String color,

        @Schema(description = "재고 수량 (0 이상)", example = "10")
        @NotNull
        @PositiveOrZero
        Integer stock

) {}
