package com.example.demo.api.common.api;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>("200", "성공", data);
    }

    public static ApiResponse<Void> ok(){
        return new ApiResponse<>("200", "성공", null);
    }

    public static ApiResponse<Void> badRequest(String message){
        return new ApiResponse<>("400", message, null);
    }

    public static ApiResponse<Void> badRequest(){
        return new ApiResponse<>("400", null, null);
    }

    public static ApiResponse<Void> unauthorized(){
        return new ApiResponse<>("401", "unauthorized", null);
    }

    public static ApiResponse<Void> error(String message){
        return new ApiResponse<>("500", message, null);
    }
}
