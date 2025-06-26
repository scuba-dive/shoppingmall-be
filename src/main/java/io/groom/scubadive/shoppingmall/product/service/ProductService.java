package io.groom.scubadive.shoppingmall.product.service;


import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.groom.scubadive.shoppingmall.category.repository.CategoryRepository;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionStatus;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductSaveRequest;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductSaveResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductWithOptionPageResponse;
import io.groom.scubadive.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ApiResponseDto<ProductSaveResponse> createProduct(ProductSaveRequest request) {
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(
                () -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND)
        );

        Product product = Product.createProduct(request.name(), request.description(), request.price(), getRandomNumber().longValue(), generateRandomRating(), category);
        productRepository.save(product);

        // 해당 카테고리 안에 상품이 얼마나 있는지 확인.
        Long count = productRepository.countOptionsByCategoryId(category.getId());

        AtomicInteger skuCount = new AtomicInteger(count.byteValue() + 1);
        List<ProductOption> options = request.options().stream().map(productOptionRequest -> {

            String sku = generateSku(category.getName(), productOptionRequest.color(), skuCount.getAndIncrement());

            return ProductOption.createProductOption(productOptionRequest.color(), sku, productOptionRequest.stock().longValue(), ProductOptionStatus.ACTIVE, product);
        }).toList();

        product.addOptions(options);

        productRepository.flush();

        return ApiResponseDto.of(201, "상품이 성공적으로 등록되었습니다.", ProductSaveResponse.from(product));
    }


    public ApiResponseDto<ProductWithOptionPageResponse> getProductsByPageable(Pageable pageable) {
        Page<ProductOption> productOptionPageable = productRepository.findProductOptionPageable(pageable);

        return ApiResponseDto.of(200, "성공적으로 조회했습니다.", ProductWithOptionPageResponse.from(productOptionPageable));
    }


    public ApiResponseDto<Void> deleteProductById(Long id) {
        productRepository.deleteById(id);
        return ApiResponseDto.of(200, "상품이 성공적으로 삭제되었습니다.", null);
    }

    private Integer getRandomNumber() {
        return new Random().nextInt(1000) + 1; // 1 ~ 1000
    }

    private BigDecimal generateRandomRating() {
        double random = ThreadLocalRandom.current().nextDouble(0.0, 100.0); // 원하는 범위 지정
        return BigDecimal.valueOf(random).setScale(2, RoundingMode.HALF_UP); // 소수점 둘째자리까지
    }

    private String generateSku(String categoryName, String color, int index) {
        return String.format("%s-%s-%03d",
                categoryName.toUpperCase(),
                colorToCode(color),
                index
        );
    }

    private String colorToCode(String color) {
        return switch (color) {
            case "빨간색" -> "RED";
            case "주황색" -> "ORANGE";
            case "노란색" -> "YELLOW";
            case "초록색" -> "GREEN";
            case "파란색" -> "BLUE";
            case "남색" -> "NAVY";
            case "보라색" -> "PURPLE";
            default -> "GEN";
        };
    }
}
