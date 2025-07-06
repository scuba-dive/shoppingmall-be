package io.groom.scubadive.shoppingmall.payment.repository;

import io.groom.scubadive.shoppingmall.payment.domain.PaymentPending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentPendingRepository extends JpaRepository<PaymentPending, Long> {
    Optional<PaymentPending> findByOrderId(String orderId);
}

