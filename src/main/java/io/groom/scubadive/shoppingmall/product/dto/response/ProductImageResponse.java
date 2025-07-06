package io.groom.scubadive.shoppingmall.product.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageResponse {

    @Schema(description = "상품 옵션 ID", example = "1")
    private Long id;

    @Schema(description = "상품 옵션 이미지 URL", example = "https://my-shop-image-bucket.s3.ap-northeast-2.amazonaws.com/product/bed1-blue.webp")
    private String imageUrl;
}