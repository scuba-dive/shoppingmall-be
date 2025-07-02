package io.groom.scubadive.shoppingmall.member.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_paid")
public class UserPaid extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private long amount;

    public UserPaid(User user) {
        this.user = user;
        this.count = 0;
        this.amount = 0L;
    }


    public void addPayment(long paymentAmount) {
        this.amount += paymentAmount;
        this.count += 1;
    }
}
