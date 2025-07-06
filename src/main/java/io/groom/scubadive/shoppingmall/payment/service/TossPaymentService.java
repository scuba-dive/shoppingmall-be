package io.groom.scubadive.shoppingmall.payment.service;

import io.groom.scubadive.shoppingmall.cart.domain.CartItem;
import io.groom.scubadive.shoppingmall.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final CartItemRepository cartItemRepository;


    public String createTossPaymentUrl(Long cartId, List<Long> cartItemIds) {
        List<CartItem> selectedItems = cartItemRepository.findAllById(cartItemIds);

        long amount = selectedItems.stream()
                .mapToLong(item -> item.getProductOption().getProduct().getPrice() * item.getQuantity())
                .sum();

        // 대표 상품명 구성
        String orderName = selectedItems.get(0).getProductOption().getProduct().getProductName();
        if (selectedItems.size() > 1) {
            orderName += " 외 " + (selectedItems.size() - 1) + "건";
        }

        // 모의 결제 URL 생성
        return "https://mock.tosspayments.com/pay/tx-" + UUID.randomUUID()
                + "?amount=" + amount + "&orderName=" + orderName;
    }
}
