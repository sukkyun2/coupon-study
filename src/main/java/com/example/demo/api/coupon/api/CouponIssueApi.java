package com.example.demo.api.coupon.api;

import com.example.demo.api.common.api.ApiResponse;
import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.coupon.app.CouponAlreadyExistException;
import com.example.demo.api.coupon.app.CouponIssueRequest;
import com.example.demo.api.coupon.app.CouponIssueService;
import com.example.demo.api.coupon.domain.NoQuantityException;
import com.example.demo.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponIssueApi {
    private final CouponIssueService couponIssueService;

    @PostMapping("/api/v1/coupons/{couponId}/issue")
    public ApiResponse<?> issueCoupon(@PathVariable Integer couponId, User user) {
        try {
            couponIssueService.issueCoupon(new CouponIssueRequest(user.getId(), couponId));
        } catch (NoDataException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (NoQuantityException | CouponAlreadyExistException e){
            return ApiResponse.ok(e.getMessage());
        }

        return ApiResponse.ok();
    }
}
