package io.groom.scubadive.shoppingmall.global.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentry")
public class SentryTestController {

    @GetMapping("/test-error")
    public String testError() {
        throw new RuntimeException("Sentry 연동 테스트 예외");
    }
}
