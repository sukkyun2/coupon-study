package com.example.demo.api.coupon.api;

import com.example.demo.api.common.api.ApiResponse;
import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.coupon.app.CouponAlreadyExistException;
import com.example.demo.api.coupon.app.CouponIssueRequest;
import com.example.demo.api.coupon.app.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponIssueApi {
    private final CouponIssueService couponIssueService;

    @PostMapping("/api/v1/coupons/{couponId}/issue")
    public ApiResponse<Void> issueCoupon(@PathVariable Integer couponId, @RequestParam Integer userId) {
        try {
            couponIssueService.issueCoupon(new CouponIssueRequest(userId, couponId));
        } catch (NoDataException | CouponAlreadyExistException e) {
            return ApiResponse.badRequest(e.getMessage());
        }

        return ApiResponse.ok();
    }
}
