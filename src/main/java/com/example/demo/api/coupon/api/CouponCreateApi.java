package com.example.demo.api.coupon.api;

import com.example.demo.api.common.api.ApiResponse;
import com.example.demo.api.common.app.ValidationException;
import com.example.demo.api.coupon.app.CouponCreateRequest;
import com.example.demo.api.coupon.app.CouponCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponCreateApi {
    private final CouponCreateService couponCreateService;

    @PostMapping("/api/v1/coupons")
    public ApiResponse<?> createCoupon(@RequestBody CouponCreateRequest req) {
        try {
            return ApiResponse.ok(couponCreateService.createCoupon(req));
        } catch (ValidationException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
}
