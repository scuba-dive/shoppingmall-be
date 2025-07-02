package io.groom.scubadive.shoppingmall.product.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductSaveRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductStatusUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductStockUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductSaveResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUpdateResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductWithOptionPageResponse;
import io.groom.scubadive.shoppingmall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin - 상품 API", description = "관리자용 상품 등록, 수정, 삭제, 조회 기능을 제공합니다.")
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @Operation(summary = "상품 전체 조회 (관리자)", description = "관리자용 전체 상품과 옵션 정보를 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<ProductWithOptionPageResponse> getProducts(Pageable pageable) {
        return productService.getProductsByPageable(pageable);
    }

    @Operation(summary = "상품 등록", description = "신규 상품과 옵션 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<ProductSaveResponse> createProduct(@RequestBody ProductSaveRequest request) {
        return productService.createProduct(request);
    }

    @Operation(summary = "상품 수정", description = "기존 상품의 설명 및 가격을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<ProductUpdateResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
    ) {
        return productService.updateProductById(id, request);
    }

    @Operation(summary = "상품 삭제", description = "지정된 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    @Operation(summary = "상품 옵션 상태 변경", description = "상품 옵션의 상태(ACTIVE/SOLD_OUT)를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "상품 옵션을 찾을 수 없음")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody ProductStatusUpdateRequest request
    ) {
        return productService.updateStatusByOptionId(id, request);
    }

    @Operation(summary = "상품 옵션 재고 수정", description = "상품 옵션의 재고 수량을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재고 수정 성공"),
            @ApiResponse(responseCode = "404", description = "상품 옵션을 찾을 수 없음")
    })
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<Void> updateStock(
            @PathVariable Long id,
            @RequestBody ProductStockUpdateRequest request
    ) {
        return productService.updateStockByOptionId(id, request);
    }
}
