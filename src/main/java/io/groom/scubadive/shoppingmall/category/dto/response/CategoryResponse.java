package io.groom.scubadive.shoppingmall.category.dto.response;

import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답 DTO")
public record CategoryResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long id,

        @Schema(description = "카테고리명", example = "의자")
        String name

) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getKoreanName());
    }
}
