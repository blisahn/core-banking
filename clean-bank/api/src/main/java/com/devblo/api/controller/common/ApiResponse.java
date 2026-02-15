package com.devblo.api.controller.common;

public record ApiResponse<T>(
        boolean success,
        T data,
        String errorMessage,
        String errorCode
) {
    @SuppressWarnings("unchecked")
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

//    public static <T> ApiResponse<T> success() {
//        return new ApiResponse<>(true, null, null, null);
//    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, null, message, "BAD_REQUEST");
    }
}
