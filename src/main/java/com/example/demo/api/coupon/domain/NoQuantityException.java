package com.example.demo.api.coupon.domain;

public class NoQuantityException extends RuntimeException {
    public NoQuantityException() {
        super("쿠폰 수량이 부족합니다");
    }
}
