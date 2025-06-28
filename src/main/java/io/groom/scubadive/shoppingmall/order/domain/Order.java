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

    private int totalCount;
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(User user, String orderNumber, int totalCount, Long totalPrice, OrderStatus orderStatus) {
        this.user = user;
        this.orderNumber = orderNumber;
        this.totalCount = totalCount;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }
}