package io.groom.scubadive.shoppingmall.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> of(int status, String message, T data) {
        return new ApiResponseDto<>(status, message, data);
    }

    public static <T> ApiResponseDto<T> error(int status, String message) {
        return new ApiResponseDto<>(status, message, null);
    }
}
