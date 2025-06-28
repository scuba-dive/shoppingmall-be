package io.groom.scubadive.shoppingmall.member.util;

import java.util.List;
import java.util.Random;

public class NicknameGenerator {

    private static final List<String> adjectives = List.of(
            "불타는", "빛나는", "웃는", "반짝이는", "날아가는", "멈추지않는",
            "달리는", "노래하는", "춤추는", "터지는", "지글거리는", "터질듯한"
    );

    private static final List<String> fruits = List.of(
            "수박", "참외", "딸기", "포도", "망고", "복숭아", "귤", "자두", "멜론", "체리"
    );

    private static final Random random = new Random();

    public static String generate() {
        String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String fruit = fruits.get(random.nextInt(fruits.size()));
        return adjective + " " + fruit;
    }

    public static String generateWithNumberSuffix() {
        String nickname = generate();
        int number = 1 + random.nextInt(1000); // 1~999
        return nickname + number;
    }
}
