package io.groom.scubadive.shoppingmall.product.dto.request;

import io.groom.scubadive.shoppingmall.product.domain.ProductOptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProductStatusUpdateRequest(

        @Schema(
                description = "상품 옵션 상태 (ACTIVE 또는 SOLD_OUT)",
                example = "ACTIVE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ProductOptionStatus status

) {}
