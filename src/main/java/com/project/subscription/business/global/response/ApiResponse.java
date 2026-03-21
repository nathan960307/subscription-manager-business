package com.project.subscription.business.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 2000, "성공",LocalDateTime.now(), data);
    }

    public static ApiResponse<?> fail(int code, String message) {
        return new ApiResponse<>(false, code, message,LocalDateTime.now(), null);
    }

}
