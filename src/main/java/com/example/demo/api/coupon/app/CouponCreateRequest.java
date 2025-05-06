package com.example.demo.api.coupon.app;

import com.example.demo.api.coupon.domain.enums.CouponType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponCreateRequest(
        String name,
        int amount,
        CouponType couponType,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime expiredAt
) {
}
