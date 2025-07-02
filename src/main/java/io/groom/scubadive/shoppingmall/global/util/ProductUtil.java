package io.groom.scubadive.shoppingmall.global.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ProductUtil {
    public Integer getRandomNumber() {
        return new Random().nextInt(1000) + 1; // 1 ~ 1000
    }

    public BigDecimal generateRandomRating() {
        double random = ThreadLocalRandom.current().nextDouble(0.0, 100.0); // 원하는 범위 지정
        return BigDecimal.valueOf(random).setScale(2, RoundingMode.HALF_UP); // 소수점 둘째자리까지
    }

    public String generateSku(String categoryName, String color, int index) {
        return String.format("%s-%s-%03d",
                categoryName.toUpperCase(),
                colorToCode(color),
                index
        );
    }

    public String colorToCode(String color) {
        return switch (color) {
            case "빨간색" -> "RED";
            case "주황색" -> "ORANGE";
            case "노란색" -> "YELLOW";
            case "초록색" -> "GREEN";
            case "파란색" -> "BLUE";
            case "남색" -> "NAVY";
            case "보라색" -> "PURPLE";
            case "검은색" -> "BLACK";
            default -> "GEN";
        };
    }
}
