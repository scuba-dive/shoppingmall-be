package io.groom.scubadive.shoppingmall.category.service;

import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.groom.scubadive.shoppingmall.category.dto.response.CategoryResponse;
import io.groom.scubadive.shoppingmall.category.repository.CategoryRepository;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public ApiResponseDto<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return ApiResponseDto.of(200, "카테고리 목록을 성공적으로 불러왔습니다.", categories.stream().map(CategoryResponse::from).toList());
    }
}
