package io.groom.scubadive.shoppingmall.member.domain.enums;

public enum UserStatus {

    ACTIVE,           // 활성 사용자
    DORMANT_AUTO,     // 자동 휴면 (3개월 이상 미접속)
    DORMANT_MANUAL    // 수동 휴면 (관리자 또는 사용자가 직접 상태 변경)
}
