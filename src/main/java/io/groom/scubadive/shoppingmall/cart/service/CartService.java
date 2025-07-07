package io.groom.scubadive.shoppingmall.cart.service;

import io.groom.scubadive.shoppingmall.cart.domain.*;
import io.groom.scubadive.shoppingmall.cart.dto.request.*;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartItemResponse;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartResponse;
import io.groom.scubadive.shoppingmall.cart.repository.*;
import io.groom.scubadive.shoppingmall.global.exception.ErrorCode;
import io.groom.scubadive.shoppingmall.global.exception.GlobalException;
import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import io.groom.scubadive.shoppingmall.product.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public CartItemResponse addItem(Long userId, CartItemRequest request) {

        if (request.getQuantity() <= 0) {
            throw new GlobalException(ErrorCode.INVALID_CART_QUANTITY);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        ProductOption option = productOptionRepository.findById(request.getProductOptionId())
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));

        // 기존에 같은 productOptionId가 있는지 확인
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductOptionId(cart.getId(), option.getId());

        CartItem item;
        if (existingItemOpt.isPresent()) {
            // 이미 있으면 수량 증가
            item = existingItemOpt.get();
            item.addQuantity(request.getQuantity()); // 엔티티에서 quantity += 추가된 수량
        } else {
            // 없으면 새로 추가
            item = new CartItem(cart, option, request.getQuantity());
            cartItemRepository.save(item);
        }

        Long price = option.getProduct().getPrice();
        int quantity = item.getQuantity();

        return CartItemResponse.builder()
                .cartItemId(item.getId())
                .productId(option.getProduct().getId())
                .productOptionId(option.getId())
                .productName(option.getProduct().getProductName())
                .color(option.getColor())
                .price(price)
                .quantity(quantity)
                .totalPricePerItem(price * quantity)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    @Transactional
    public CartItemResponse updateItem(Long cartItemId, CartUpdateRequest request) {
        if (request.getQuantity() <= 0) {
            throw new GlobalException(ErrorCode.INVALID_CART_QUANTITY);
        }

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 항목을 찾을 수 없습니다."));

        item.changeQuantity(request.getQuantity());

        Long price = item.getProductOption().getProduct().getPrice();
        int quantity = item.getQuantity();

        return CartItemResponse.builder()
                .cartItemId(item.getId())
                .productId(item.getProductOption().getProduct().getId())
                .productOptionId(item.getProductOption().getId())
                .productName(item.getProductOption().getProduct().getProductName())
                .color(item.getProductOption().getColor())
                .price(price)
                .quantity(quantity)
                .totalPricePerItem(price * quantity)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    public CartResponse getCartResponse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("장바구니가 없습니다."));

        List<CartItemResponse> items = cart.getItems().stream().map(item -> {
            Long price = item.getProductOption().getProduct().getPrice();
            int quantity = item.getQuantity();
            return CartItemResponse.builder()
                    .cartItemId(item.getId())
                    .productId(item.getProductOption().getProduct().getId())
                    .productOptionId(item.getProductOption().getId())
                    .productName(item.getProductOption().getProduct().getProductName())
                    .color(item.getProductOption().getColor())
                    .price(price)
                    .quantity(quantity)
                    .totalPricePerItem(price * quantity)
                    .createdAt(item.getCreatedAt())
                    .updatedAt(item.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());

        int totalQuantity = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        Long totalAmount = items.stream().mapToLong(CartItemResponse::getTotalPricePerItem).sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .totalQuantity(totalQuantity)
                .totalAmount(totalAmount)
                .build();
    }

    @Transactional
    public void deleteItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_DELETED));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("장바구니가 없습니다."));
        cart.getItems().clear();
    }
}