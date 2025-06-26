package io.groom.scubadive.shoppingmall.product.dto.request;

public record ProductUpdateRequest(
        Long price,
        String description
) {
}
