package com.example.demo.api.common.app;

public class NoDataException extends RuntimeException {
    public NoDataException() {
        super("데이터가 없습니다");
    }
}
