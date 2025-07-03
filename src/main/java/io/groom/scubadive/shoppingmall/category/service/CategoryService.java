package io.groom.scubadive.shoppingmall.category.service;

import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.groom.scubadive.shoppingmall.category.dto.response.CategoryProductUserPageResponse;
import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.category.repository.CategoryRepository;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public ApiResponseDto<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return ApiResponseDto.of(200, "카테고리 목록을 성공적으로 불러왔습니다.", categories.stream().map(CategoryResponse::from).toList());
    }

    public ApiResponseDto<CategoryProductUserPageResponse> getCategoryProductUserPage(Long categoryId, Pageable pageable) {
        Page<Product> productsByCategoryId = productRepository.findProductsByCategoryId(categoryId, pageable);

        return ApiResponseDto.of(200, "상품 목록을 성공적으로 불러왔습니다.", CategoryProductUserPageResponse.from(productsByCategoryId));
    }



    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND)
        );
    }

}
