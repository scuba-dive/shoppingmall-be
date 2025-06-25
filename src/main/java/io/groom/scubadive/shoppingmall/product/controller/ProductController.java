package io.groom.scubadive.shoppingmall.product.controller;

import io.groom.scubadive.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;



}
