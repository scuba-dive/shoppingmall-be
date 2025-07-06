package io.groom.scubadive.shoppingmall.product.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ProductCreateRequest {
    private String productName;
    private String description;
    private Long price;
    private String categoryName; // 한글명 ("의자", "책상" ...)
    private List<ProductOptionRequest> options;

    @Getter
    public static class ProductOptionRequest {
        private String color; // "파랑", "빨강", "검정"
    }
}
