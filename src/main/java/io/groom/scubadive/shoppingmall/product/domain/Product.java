package io.groom.scubadive.shoppingmall.product.domain;


import io.groom.scubadive.shoppingmall.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;


    private Long price;

    private Long reviewCount;

    @Column(precision = 10, scale = 2)
    private BigDecimal rating;
}
