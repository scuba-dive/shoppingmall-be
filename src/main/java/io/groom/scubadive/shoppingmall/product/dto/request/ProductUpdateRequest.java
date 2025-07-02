package io.groom.scubadive.shoppingmall.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(

        @PositiveOrZero
        @Schema(
                description = "상품 가격",
                example = "29900",
                minimum = "0"
        )
        Long price,

        @Size(max = 1000)
        @Schema(
                description = "상품 설명",
                example = "이 상품은 내구성이 뛰어나고 여행 시 매우 유용합니다.",
                maxLength = 1000
        )
        String description

) {}
