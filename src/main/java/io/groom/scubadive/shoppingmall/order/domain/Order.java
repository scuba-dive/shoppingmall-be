package io.groom.scubadive.shoppingmall.order.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import io.groom.scubadive.shoppingmall.member.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(User user, String orderNumber, int totalQuantity, Long totalAmount, OrderStatus status) {
        this.user = user;
        this.orderNumber = orderNumber;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }
}