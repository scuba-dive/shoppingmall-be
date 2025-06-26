package io.groom.scubadive.shoppingmall.product.service;


import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.groom.scubadive.shoppingmall.category.repository.CategoryRepository;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.global.util.ProductUtil;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionStatus;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductSaveRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductStatusUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductStockUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductSaveResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductUpdateResponse;
import io.groom.scubadive.shoppingmall.product.dto.response.ProductWithOptionPageResponse;
import io.groom.scubadive.shoppingmall.product.repository.ProductOptionRepository;
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
    private final ProductOptionRepository productOptionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductUtil productUtil;

    public ApiResponseDto<ProductSaveResponse> createProduct(ProductSaveRequest request) {
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(
                () -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND)
        );


        Product product = Product.createProduct(request.name(), request.description(), request.price(), productUtil.getRandomNumber().longValue(), productUtil.generateRandomRating(), category);
        productRepository.save(product);

        // 해당 카테고리 안에 상품이 얼마나 있는지 확인.
        Long count = productRepository.countOptionsByCategoryId(category.getId());

        AtomicInteger skuCount = new AtomicInteger(count.byteValue() + 1);
        List<ProductOption> options = request.options().stream().map(productOptionRequest -> {

            String sku = productUtil.generateSku(category.getName(), productOptionRequest.color(), skuCount.getAndIncrement());

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

    public ApiResponseDto<ProductUpdateResponse> updateProductById(Long id, ProductUpdateRequest request) {
        Product product = findProductById(id);

        product.updateDescription(request.description());
        product.updatePrice(request.price());
        productRepository.flush();

        return ApiResponseDto.of(200, "상품이 성공적으로 수정되었습니다.", ProductUpdateResponse.from(product));
    }


    public ApiResponseDto<Void> deleteProductById(Long id) {
        Product product = findProductById(id);
        productRepository.deleteById(product.getId());
        return ApiResponseDto.of(200, "상품이 성공적으로 삭제되었습니다.", null);
    }

    public ApiResponseDto<Void> updateStockByOptionId(Long id, ProductStockUpdateRequest request) {
        ProductOption productOption = findProductOptionById(id);

        productOption.updateStock(request.stock());

        return ApiResponseDto.of(200, "재고 수량이 변경되었습니다.", null);
    }

    public ApiResponseDto<Void> updateStatusByOptionId(Long id, ProductStatusUpdateRequest request) {
        ProductOption productOption = findProductOptionById(id);

        productOption.updateStatus(request.status());

        return ApiResponseDto.of(200, "상품 상태가 변경되었습니다.", null);

    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND)
        );
    }

    private ProductOption findProductOptionById(Long id) {
        return productOptionRepository.findById(id).orElseThrow(
                () -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND)
        );
    }
}
