package io.groom.scubadive.shoppingmall.payment.service;

import io.groom.scubadive.shoppingmall.cart.domain.CartItem;
import io.groom.scubadive.shoppingmall.cart.repository.CartItemRepository;
import io.groom.scubadive.shoppingmall.global.config.TossPaymentConfig;
import io.groom.scubadive.shoppingmall.order.dto.request.OrderCreateRequest;
import io.groom.scubadive.shoppingmall.order.service.OrderService;
import io.groom.scubadive.shoppingmall.payment.domain.PaymentPending;
import io.groom.scubadive.shoppingmall.payment.dto.response.TossApproveResponse;
import io.groom.scubadive.shoppingmall.payment.dto.response.TossPaymentReadyResponse;
import io.groom.scubadive.shoppingmall.payment.repository.PaymentPendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final CartItemRepository cartItemRepository;
    private final PaymentPendingRepository paymentPendingRepository;
    private final TossPaymentConfig tossProps;
    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderService orderService;

    public TossPaymentReadyResponse createTossPaymentReady(
            Long userId, Long cartId, List<Long> cartItemIds, String username) {

        List<CartItem> selectedItems = cartItemRepository.findAllById(cartItemIds);
        long amount = selectedItems.stream()
                .mapToLong(item -> item.getProductOption().getProduct().getPrice() * item.getQuantity())
                .sum();

        String orderName = selectedItems.get(0).getProductOption().getProduct().getProductName();
        if (orderName.length() > 100) orderName = orderName.substring(0, 100);

        String orderId = UUID.randomUUID().toString();
        String customerName = (username == null || username.isBlank()) ? "비회원" : username;

        // Pending DB에 저장
        PaymentPending pending = PaymentPending.builder()
                .orderId(orderId)
                .userId(userId)
                .cartId(cartId)
                .amount(amount)
                .status("PENDING")
                .cartItemIds(cartItemIds.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();
        paymentPendingRepository.save(pending);

        return new TossPaymentReadyResponse(orderId, amount, orderName, customerName);
    }

    public TossApproveResponse approvePayment(String paymentKey, String orderId, Long amount) {
        // Pending 검증
        PaymentPending pending = paymentPendingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 세션이 유효하지 않습니다. 다시 시도해주세요."));

        if (!pending.getAmount().equals(amount)) {
            throw new RuntimeException("결제 금액이 일치하지 않습니다.");
        }
        if (!"PENDING".equals(pending.getStatus())) {
            throw new RuntimeException("이미 승인/실패 처리된 주문입니다.");
        }

        // Toss 결제 승인 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(tossProps.getTestSecretKey(), "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("paymentKey", paymentKey);
        payload.put("orderId", orderId);
        payload.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    request,
                    Map.class
            );
            Map<String, Object> body = Objects.requireNonNull(response.getBody());

            // 결제 성공시 pending → APPROVED
            pending.setStatus("APPROVED");
            paymentPendingRepository.save(pending);

            /*** 👇 주문 생성 로직 추가 부분 ***/
            Long userId = pending.getUserId();
            Long cartId = pending.getCartId(); // cartId가 PaymentPending에 있다면!
            List<Long> cartItemIds = Arrays.stream(pending.getCartItemIds().split(","))
                    .map(Long::parseLong)
                    .toList();

            // OrderCreateRequest 생성자/빌더 필요
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(cartId, cartItemIds);

            // OrderService 주입 필요 (예: 필드로 @Autowired 혹은 @RequiredArgsConstructor로)
            orderService.createOrder(userId, orderCreateRequest);

            /*** 👆 주문 생성 끝 ***/

            return TossApproveResponse.of(body);

        } catch (RestClientResponseException e) {
            // 결제 실패시 pending → FAILED
            pending.setStatus("FAILED");
            paymentPendingRepository.save(pending);
            throw new RuntimeException("결제 승인 실패: " + e.getResponseBodyAsString());
        }
    }
}