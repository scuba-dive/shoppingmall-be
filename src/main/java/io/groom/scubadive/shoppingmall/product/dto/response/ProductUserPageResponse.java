package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductUserPageResponse(
        long total,
        int page,
        int size,
        List<ProductUserResponse> products
) {

    public static ProductUserPageResponse from(Page<Product> productPage) {
        List<ProductUserResponse> results = productPage.getContent().stream().map(ProductUserResponse::from).toList();

        return new ProductUserPageResponse(
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize(),
                results
        );
    }
}
