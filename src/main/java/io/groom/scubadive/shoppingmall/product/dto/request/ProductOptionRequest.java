package io.groom.scubadive.shoppingmall.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductOptionRequest(
        @NotBlank
        String color,

        @NotNull
        @PositiveOrZero
        Integer stock
) {
}
