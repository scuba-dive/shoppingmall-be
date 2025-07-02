package io.groom.scubadive.shoppingmall.product.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductDetailUserResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUserPageResponse;
import io.groom.scubadive.shoppingmall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Public API", description = "비회원 공개 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 전체 리스트 조회", description = "사용자용 전체 상품 목록을 페이지네이션으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    })
    @GetMapping
    public ApiResponseDto<ProductUserPageResponse> getProducts(@PageableDefault(size = 8) Pageable pageable) {
        return productService.getProductUsersByPageable(pageable);
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID를 통해 단일 상품의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ApiResponseDto<ProductDetailUserResponse> getProduct(@PathVariable Long id) {
        return productService.findProductDetailUserById(id);
    }
}
