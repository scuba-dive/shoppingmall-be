package io.groom.scubadive.shoppingmall.product.service;


import io.groom.scubadive.shoppingmall.category.domain.Category;
import io.groom.scubadive.shoppingmall.category.repository.CategoryRepository;
import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.global.util.ProductUtil;
import io.groom.scubadive.shoppingmall.global.util.S3Uploader;
import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionImage;
import io.groom.scubadive.shoppingmall.product.domain.ProductOptionStatus;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductCreateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductSaveRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductStockUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.request.ProductUpdateRequest;
import io.groom.scubadive.shoppingmall.product.dto.response.*;
import io.groom.scubadive.shoppingmall.product.repository.ProductOptionRepository;
import io.groom.scubadive.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductUtil productUtil;
    private final S3Uploader s3Uploader;

    @Transactional
    public Product createProduct(ProductCreateRequest request, List<MultipartFile> optionImages) {
        // 0. 한글명 → 카테고리 조회
        Category category = categoryRepository.findByKoreanName(request.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        if (!isValidCategoryKoreanName(category.getKoreanName())) {
            throw new IllegalArgumentException("카테고리는 의자, 책상, 침대, 수납장, 소품만 등록할 수 있습니다.");
        }

        // 1. 옵션 3개(파랑, 빨강, 검정) 필수 검증
        List<ProductCreateRequest.ProductOptionRequest> optionList = request.getOptions();
        List<String> requiredColors = List.of("파랑", "빨강", "검정");
        List<String> optionColors = optionList.stream()
                .map(ProductCreateRequest.ProductOptionRequest::getColor)
                .toList();

        for (String required : requiredColors) {
            if (!optionColors.contains(required)) {
                throw new IllegalArgumentException("옵션에는 반드시 파랑, 빨강, 검정 3가지 색상이 모두 포함되어야 합니다.");
            }
        }

        if (optionList.size() != requiredColors.size() || optionImages.size() != requiredColors.size()) {
            throw new IllegalArgumentException("옵션 및 이미지 개수는 파랑, 빨강, 검정 3개여야 합니다.");
        }

        // 2. 상품 생성
        Product product = Product.createProduct(
                request.getProductName(),
                request.getDescription(),
                request.getPrice(),
                0L, // 리뷰수
                BigDecimal.ZERO, // 평점
                category
        );

        // 3. 옵션 생성 (stock=0, sku 자동, color 변환, imagePath 규칙)
        for (int i = 0; i < optionList.size(); i++) {
            ProductCreateRequest.ProductOptionRequest optionDto = optionList.get(i);
            MultipartFile optionImage = optionImages.get(i);

            // 이미지 누락 체크
            if (optionImage == null || optionImage.isEmpty()) {
                throw new IllegalArgumentException("모든 옵션에 이미지를 한 장씩 첨부해야 합니다.");
            }

            String englishColor = convertKoreanToEnglishColor(optionDto.getColor());
            String categoryCode = category.getCategoryName();
            String sku = generateSku(categoryCode, englishColor);

            // [조건] imagePath: /product/{sku}.webp
            String imagePath = "/product/" + sku.toLowerCase() + ".webp";

            // S3 업로드 시 지정한 경로와 파일명으로 업로드
            try {
                s3Uploader.upload(optionImage, imagePath);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }

            ProductOption option = ProductOption.createProductOption(
                    englishColor,
                    sku,
                    0L, // 재고는 무조건 0
                    ProductOptionStatus.ACTIVE,
                    product
            );

            ProductOptionImage optionImageEntity = ProductOptionImage.createProductOptionImage(
                    option,
                    imagePath, // /product/{sku}.webp 형식
                    s3Uploader.getBucketName()
            );
            option.addProductOptionImage(optionImageEntity);
            product.addOption(option);
        }

        return productRepository.save(product);
    }

    // 한글→영어 변환 (색상)
    private static final Map<String, String> KOREAN_TO_ENGLISH_COLOR = Map.of(
            "파랑", "BLUE",
            "빨강", "RED",
            "검정", "BLACK"
    );

    private String convertKoreanToEnglishColor(String koreanColor) {
        String english = KOREAN_TO_ENGLISH_COLOR.get(koreanColor);
        if (english == null) {
            throw new IllegalArgumentException("색상은 파랑, 빨강, 검정만 입력 가능합니다.");
        }
        return english;
    }

    // 5종 한글 카테고리만 허용
    private boolean isValidCategoryKoreanName(String koreanName) {
        return List.of("의자", "책상", "침대", "수납장", "소품").contains(koreanName);
    }

    // SKU 자동생성 (카테고리-색상-날짜-랜덤)
    private String generateSku(String categoryCode, String colorCode) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String random = String.format("%04d", new Random().nextInt(10000));
        return categoryCode + "-" + colorCode + "-" + date + "-" + random;
    }



    public ApiResponseDto<ProductWithOptionPageResponse> getProductsByPageable(Pageable pageable) {
        Page<ProductOption> productOptionPageable = productRepository.findProductOptionPageable(pageable);

        return ApiResponseDto.of(200, "성공적으로 조회했습니다.", ProductWithOptionPageResponse.from(productOptionPageable));
    }

    public ApiResponseDto<ProductUserPageResponse> getProductUsersByPageable(Pageable pageable) {
        Page<Product> productsPageable = productRepository.findProductsPageable(pageable);

        return ApiResponseDto.of(200, "상품 목록을 성공적으로 불러왔습니다", ProductUserPageResponse.from(productsPageable));
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

    public ApiResponseDto<Void> toggleStatusByOptionId(Long id) {
        ProductOption productOption = findProductOptionById(id);

        ProductOptionStatus currentStatus = productOption.getStatus();
        ProductOptionStatus newStatus =
                currentStatus == ProductOptionStatus.ACTIVE
                        ? ProductOptionStatus.SOLD_OUT
                        : ProductOptionStatus.ACTIVE;

        productOption.updateStatus(newStatus);

        return ApiResponseDto.of(200, "상품 옵션 상태가 " + newStatus + "로 변경되었습니다.", null);
    }

    public ApiResponseDto<ProductDetailUserResponse> findProductDetailUserById(Long id) {
        Product product = productRepository.findWithOptionsById(id).orElseThrow(
                () -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        return ApiResponseDto.of(200, "상품 정보를 성공적으로 불러왔습니다.", ProductDetailUserResponse.from(product));
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

    public ApiResponseDto<ProductImageResponse> getProductOptionImageUrl(Long id) {
        ProductOption option = productOptionRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductOptionImage image = option.getProductOptionImages().stream()
                .findFirst()
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND));
        String imageUrl = "https://" + "my-shop-image-bucket" + ".s3.ap-northeast-2.amazonaws.com" + image.getImagePath();

        ProductImageResponse imageResponse = new ProductImageResponse(id, imageUrl);
        return ApiResponseDto.of(200, "상품 옵션 이미지 URL을 성공적으로 불러왔습니다.", imageResponse);
    }



//    public ApiResponseDto<ProductSaveResponse> createProduct(ProductSaveRequest request) {
//        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(
//                () -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND)
//        );
//
//
//        Product product = Product.createProduct(request.productName(), request.description(), request.price(), productUtil.getRandomNumber().longValue(), productUtil.generateRandomRating(), category);
//        productRepository.save(product);
//
//        // 해당 카테고리 안에 상품이 얼마나 있는지 확인.
//        Long count = productRepository.countOptionsByCategoryId(category.getId());
//
//        AtomicInteger skuCount = new AtomicInteger(count.byteValue() + 1);
//        List<ProductOption> options = request.options().stream().map(productOptionRequest -> {
//
//            String sku = productUtil.generateSku(category.getCategoryName(), productOptionRequest.color(), skuCount.getAndIncrement());
//
//            return ProductOption.createProductOption(productOptionRequest.color(), sku, productOptionRequest.stock().longValue(), ProductOptionStatus.ACTIVE, product);
//        }).toList();
//
//        product.addOptions(options);
//
//        productRepository.flush();
//
//        return ApiResponseDto.of(201, "상품이 성공적으로 등록되었습니다.", ProductSaveResponse.from(product));
//    }
}
