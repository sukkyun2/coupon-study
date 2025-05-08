package com.example.demo.api.coupon.app;

public class CouponAlreadyExistException extends RuntimeException {
    public CouponAlreadyExistException() {
        super("이미 쿠폰이 발급 되었습니다");
    }
}
