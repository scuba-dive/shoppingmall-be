package io.groom.scubadive.shoppingmall.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "주문 상세 응답 DTO")
public class OrderResponse {

    @Schema(description = "주문 ID", example = "1")
    private Long orderId;

    @Schema(description = "주문 번호", example = "ORD123456789ABC")
    private String orderNumber;

    @Schema(description = "주문일시", example = "2025-07-02T14:00:00")
    private LocalDateTime orderedAt;

    @Schema(description = "주문자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "주문자 전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "배송 주소", example = "서울시 강남구 역삼동 123-45")
    private String address;

    @Schema(description = "주문 상태", example = "PAYMENT_COMPLETED")
    private String orderStatus;

    @Schema(description = "총 주문 금액", example = "100000")
    private Long totalAmount;

    @Schema(description = "총 수량", example = "3")
    private int totalQuantity;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemDto> orderItems;

    @Getter
    @Builder
    @Schema(description = "주문 상품 상세")
    public static class OrderItemDto {

        @Schema(description = "상품명", example = "스쿠버다이빙 슈트")
        private String productName;

        @Schema(description = "옵션(색상 등)", example = "RED")
        private String option;

        @Schema(description = "수량", example = "2")
        private int quantity;

        @Schema(description = "단가", example = "50000")
        private Long price;

        @Schema(description = "해당 상품 총 금액", example = "100000")
        private Long totalPricePerItem;
    }
}