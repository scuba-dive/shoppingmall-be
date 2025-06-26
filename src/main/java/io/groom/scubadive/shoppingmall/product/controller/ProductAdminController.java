package io.groom.scubadive.shoppingmall.product.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductSaveRequest;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductSaveResponse;
import io.groom.scubadive.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductService productService;


    /**
     * 상품 전체 조회(관리자)
     * @return
     */
    @GetMapping()
    public ApiResponseDto<Void> getProducts() {
        return null;
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

    /**
     * 상품 추가
     * @param
     * @return
     */
    @PostMapping()
    public ApiResponseDto<ProductSaveResponse> createProduct(@RequestBody ProductSaveRequest request) {
        return productService.createProduct(request);
    }

    /**
     * 상품 수정
     * @param id
     * @param product
     * @return
     */
    @PatchMapping("/{id}")
    public ApiResponseDto<Void> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return null;
    }

    /**
     * 상품 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deleteProduct(@PathVariable Long id) {
        return null;
    }

    /**
     * 상품 상태 변경
     * @param id
     * @return
     */
    @PatchMapping("/{id}/status")
    public ApiResponseDto<Void> updateStatus(@PathVariable Long id) {
        return null;
    }

    /**
     * 상품 수량 변경
     * @param id
     * @return
     */
    @PatchMapping("/{id}/stock")
    public ApiResponseDto<Void> updateStock(@PathVariable Long id) {
        return null;
    }
}
