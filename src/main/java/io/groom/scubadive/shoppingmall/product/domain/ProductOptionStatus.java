package io.groom.scubadive.shoppingmall.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductOptionStatus {
    ACTIVE("active"),
    SOLD_OUT("sold-out"),
    ;
    private String value;
}
