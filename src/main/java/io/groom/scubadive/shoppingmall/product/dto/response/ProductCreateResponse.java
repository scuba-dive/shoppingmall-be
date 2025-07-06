package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductCreateResponse {
    private Long id;
    private String productName;
    private List<OptionDto> options;

    @Getter
    @AllArgsConstructor
    public static class OptionDto {
        private String color;
        private String sku;
        private Long stock;
        private List<String> images;
    }

    public static ProductCreateResponse from(Product product) {
        List<OptionDto> optionDtos = product.getOptions().stream()
                .map(opt -> new OptionDto(
                        opt.getColor(),
                        opt.getSku(),
                        opt.getStock(),
                        opt.getProductOptionImages().stream()
                                .map(ProductOptionImage::getImageUrl)
                                .toList()
                ))
                .toList();

        return new ProductCreateResponse(product.getId(), product.getProductName(), optionDtos);
    }
}
