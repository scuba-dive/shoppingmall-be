package io.groom.scubadive.shoppingmall.cart.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import io.groom.scubadive.shoppingmall.member.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public void clearItems() {
        this.items.clear();
    }

    public Cart(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user.getCart() != this) {
            user.assignCart(this);
        }
    }
}
