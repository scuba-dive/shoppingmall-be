package io.groom.scubadive.shoppingmall.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductStockUpdateRequest(

        @Schema(
                description = "변경할 재고 수량",
                example = "15",
                minimum = "0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long stock

) {}
