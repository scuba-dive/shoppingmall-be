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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartItemResponse addItem(User user, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        ProductOption option = productOptionRepository.findById(request.getProductOptionId())
                .orElseThrow(() -> new IllegalArgumentException("상품 옵션을 찾을 수 없습니다."));

        CartItem item = new CartItem(cart, option, request.getQuantity());
        cartItemRepository.save(item);

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

    public CartItemResponse updateItem(Long cartItemId, CartUpdateRequest request) {
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

    public CartResponse getCartResponse(User user) {
//        Cart cart = getOrCreateCart(user.getId());
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user)));
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
    public void clearCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user)));
        cart.getItems().clear();
    }
    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }
}