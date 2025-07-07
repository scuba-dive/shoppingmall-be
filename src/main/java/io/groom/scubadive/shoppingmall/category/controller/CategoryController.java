package io.groom.scubadive.shoppingmall.category.controller;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryProductUserPageResponse;
import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.category.service.CategoryService;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://shoppingmall-fe-iota.vercel.app"
        },
        allowCredentials = "true"
)
@Tag(name = "Public API", description = "비회원 공개 API")
@RestController
@RequestMapping("/api/users/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 전체 조회", description = "모든 카테고리 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    })
    @GetMapping
    public ApiResponseDto<List<CategoryResponse>> getCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "카테고리별 상품 조회", description = "카테고리 ID에 해당하는 상품 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 상품 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없습니다.")
    })
    @GetMapping("/{id}/products")
    public ApiResponseDto<CategoryProductUserPageResponse> getProducts(
            @Parameter(description = "카테고리 ID", example = "1")
            @PathVariable Long id,

            @Parameter(hidden = true)
            @PageableDefault(size = 8, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return categoryService.getCategoryProductUserPage(id, pageable);
    }
}
