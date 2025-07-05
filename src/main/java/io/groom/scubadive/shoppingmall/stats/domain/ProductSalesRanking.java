package io.groom.scubadive.shoppingmall.stats.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductSalesRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String productName;

    private Integer totalQuantity;

    private Long totalSales;

    @Setter
    private Integer ranking;
}