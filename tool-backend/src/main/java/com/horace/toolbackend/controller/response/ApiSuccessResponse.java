package com.horace.toolbackend.controller.response;

public record ApiSuccessResponse<T>(
        int code,
        String message,
        T data
) {

    public static <T> ApiSuccessResponse<T> success(String message, T data) {
        return new ApiSuccessResponse<>(200, message, data);
    }
}
