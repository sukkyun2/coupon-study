package com.example.demo.api.coupon.domain;

public class CouponUnavailableException extends RuntimeException {
    public CouponUnavailableException() {
        super("유효기간이 만료되었거나 이미 사용한 쿠폰입니다");
    }
}
