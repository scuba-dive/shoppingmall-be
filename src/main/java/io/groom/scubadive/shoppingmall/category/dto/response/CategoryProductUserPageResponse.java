package io.groom.scubadive.shoppingmall.category.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public record CategoryProductUserPageResponse(
        long total,
        int page,
        int size,
        List<ProductUserResponse> products
) {

    public static CategoryProductUserPageResponse from(Page<Product> productPage) {
        List<ProductUserResponse> results = productPage.getContent().stream().map(ProductUserResponse::from).toList();

        return new CategoryProductUserPageResponse(
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize(),
                results
        );
    }
}
