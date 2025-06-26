package io.groom.scubadive.shoppingmall.product.dto.request;

import io.groom.scubadive.shoppingmall.product.domain.ProductOptionStatus;

public record ProductStatusUpdateRequest(
        ProductOptionStatus status
) {
}
