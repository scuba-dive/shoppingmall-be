package io.groom.scubadive.shoppingmall.order.repository;

import io.groom.scubadive.shoppingmall.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}