package io.groom.scubadive.shoppingmall.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record ProductSaveRequest(
        @NotBlank
        String productName,

        @NotBlank
        String description,

        @NotNull
        @PositiveOrZero
        Long price,

        @NotNull
        Long categoryId,

        @NotEmpty
        List<ProductOptionRequest> options
) {
}
