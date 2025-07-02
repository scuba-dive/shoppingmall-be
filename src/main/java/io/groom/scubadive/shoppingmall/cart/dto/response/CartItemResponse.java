package io.groom.scubadive.shoppingmall.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "장바구니 상품 응답 DTO")
public class CartItemResponse {

    @Schema(description = "장바구니 항목 ID", example = "1")
    private Long cartItemId;

    @Schema(description = "상품 ID", example = "101")
    private Long productId;

    @Schema(description = "상품 이름", example = "소파아닌 의자")
    private String productName;

    @Schema(description = "상품 옵션 ID", example = "501")
    private Long productOptionId;

    @Schema(description = "색상", example = "BLUE")
    private String color;

    @Schema(description = "상품 가격", example = "120000")
    private Long price;

    @Schema(description = "담은 수량", example = "2")
    private int quantity;

    @Schema(description = "해당 상품 총 가격 (수량 * 단가)", example = "240000")
    private Long totalPricePerItem;

    @Schema(description = "장바구니 항목 생성일", example = "2025-07-02T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "장바구니 항목 수정일", example = "2025-07-02T12:00:00")
    private LocalDateTime updatedAt;
}
