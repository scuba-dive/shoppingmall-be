package io.groom.scubadive.shoppingmall.category.dto.response;

import io.groom.scubadive.shoppingmall.category.domain.Category;

public record CategoryResponse(
        Long id,
        String name
) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getKoreanName());
    }
}
