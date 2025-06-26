package io.groom.scubadive.shoppingmall.memeber.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPaid extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private int count = 0;

    private Long amount = 0L;

    public void addPayment(Long amount) {
        this.count += 1;
        this.amount += amount;
    }

    public UserPaid(User user) {
        this.user = user;
        this.count = 0;
        this.amount = 0L;
    }


}
