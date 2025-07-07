package io.groom.scubadive.shoppingmall.product.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductCreateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductStockUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductCreateResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUpdateResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductWithOptionPageResponse;
import io.groom.scubadive.shoppingmall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://shoppingmall-fe-iota.vercel.app"
        },
        allowCredentials = "true"
)
@Tag(name = "Admin API", description = "관리자 전용 API")
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

    @Operation(summary = "상품 등록", description = "관리자가 상품 정보를 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 등록 성공",
                    content = @Content(schema = @Schema(implementation = ProductCreateResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<ProductCreateResponse>> createProduct(
            @Parameter(
                    description = "상품 등록 요청 DTO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductCreateRequest.class),
                            examples = @ExampleObject(
                                    value = """
{
  "productName": "데코1",
  "description": "감성 인테리어 소품 세트",
  "price": 15000,
  "categoryName": "소품",
  "options": [
    {"color": "파랑"},
    {"color": "빨강"},
    {"color": "검정"}
  ]
}
"""
                            )
                    )
            )
            @RequestPart("product") ProductCreateRequest request,

            @Parameter(description = "옵션별 이미지 파일 리스트")
            @RequestPart("optionImages") List<MultipartFile> optionImages
    ) {
        System.out.println("====== 상품 등록 요청 진입! ======");
        Product product = productService.createProduct(request, optionImages);
        ProductCreateResponse response = ProductCreateResponse.from(product);

        return ResponseEntity.ok(ApiResponseDto.of(200, "상품 등록 성공", response));
    }


    @Tag(name = "프론트 미구현 API", description = "관리자 전용 API")
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

    @Tag(name = "프론트 미구현 API", description = "관리자 전용 API")
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
    public ApiResponseDto<Void> toggleStatus(@PathVariable Long id) {
        return productService.toggleStatusByOptionId(id);
    }
}
