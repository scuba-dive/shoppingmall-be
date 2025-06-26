package io.groom.scubadive.shoppingmall.product.dto.response;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductWithOptionPageResponse(
        long total,
        int page,
        int size,
        List<ProductWithOptionResponse> products
) {
    public static ProductWithOptionPageResponse from(Page<ProductOption> optionPage) {
        List<ProductWithOptionResponse> results = optionPage.getContent().stream()
                .map(option -> ProductWithOptionResponse.from(
                        option.getProduct(),
                        option
                ))
                .toList();

        return new ProductWithOptionPageResponse(
                optionPage.getTotalElements(),
                optionPage.getNumber(),
                optionPage.getSize(),
                results
        );
    }
}

