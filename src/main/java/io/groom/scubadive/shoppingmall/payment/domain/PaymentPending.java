package io.groom.scubadive.shoppingmall.payment.domain;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentPending {
    @Id
    @GeneratedValue
    private Long id;
    private String orderId;
    private Long userId;
    private Long cartId;
    private Long amount;
    private String status;
    private String cartItemIds;
}


