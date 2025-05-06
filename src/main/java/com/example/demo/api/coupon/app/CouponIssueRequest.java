package com.example.demo.api.coupon.app;

public record CouponIssueRequest(
        Integer userId,
        Integer couponId
) { }
