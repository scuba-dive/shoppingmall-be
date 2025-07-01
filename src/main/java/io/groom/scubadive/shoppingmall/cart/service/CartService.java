package io.groom.scubadive.shoppingmall.cart.service;

import io.groom.scubadive.shoppingmall.cart.domain.*;
import io.groom.scubadive.shoppingmall.cart.dto.request.*;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartItemResponse;
import io.groom.scubadive.shoppingmall.cart.dto.response.CartResponse;
import io.groom.scubadive.shoppingmall.cart.repository.*;
import io.groom.scubadive.shoppingmall.member.domain.User;
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
                .productName(option.getProduct().getName())
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
                .productName(item.getProductOption().getProduct().getName())
                .color(item.getProductOption().getColor())
                .price(price)
                .quantity(quantity)
                .totalPricePerItem(price * quantity)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    public CartResponse getCartResponse(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("장바구니가 없습니다."));

        List<CartItemResponse> items = cart.getItems().stream().map(item -> {
            Long price = item.getProductOption().getProduct().getPrice();
            int quantity = item.getQuantity();
            return CartItemResponse.builder()
                    .cartItemId(item.getId())
                    .productId(item.getProductOption().getProduct().getId())
                    .productOptionId(item.getProductOption().getId())
                    .productName(item.getProductOption().getProduct().getName())
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
                .orElseThrow(() -> new IllegalStateException("장바구니가 없습니다."));
        cart.getItems().clear();
    }
}