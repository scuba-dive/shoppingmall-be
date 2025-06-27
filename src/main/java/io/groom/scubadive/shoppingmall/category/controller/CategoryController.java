package io.groom.scubadive.shoppingmall.category.controller;

import io.groom.scubadive.shoppingmall.category.dto.response.CategoryProductUserPageResponse;
import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.category.service.CategoryService;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping
    public ApiResponseDto<List<CategoryResponse>> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}/products")
    public ApiResponseDto<CategoryProductUserPageResponse> getProducts(@PageableDefault(size = 8) Pageable pageable, @PathVariable Long id) {
        return categoryService.getCategoryProductUserPage(id, pageable);
    }
}
