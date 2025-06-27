package io.groom.scubadive.shoppingmall.product.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUserPageResponse;
import io.groom.scubadive.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;


    /**
     * 상품 전체 리스트 조회
     * @return
     */
    @GetMapping
    public ApiResponseDto<ProductUserPageResponse> getProducts(@PageableDefault(size = 8) Pageable pageable) {
        return productService.getProductUsersByPageable(pageable);
    }

    /**
     * 상품 단일 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ApiResponseDto<Void> getProduct(@PathVariable Long id) {
        return null;
    }

}
