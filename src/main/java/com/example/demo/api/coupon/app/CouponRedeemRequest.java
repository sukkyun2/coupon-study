package com.example.demo.api.coupon.app;

public record CouponRedeemRequest(
        Integer userId,
        Integer couponId
) {
}
