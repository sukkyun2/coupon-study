package com.example.demo.api.coupon.api;

import com.example.demo.api.common.api.ApiResponse;
import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.common.app.ValidationException;
import com.example.demo.api.coupon.app.CouponCreateRequest;
import com.example.demo.api.coupon.app.CouponRedeemRequest;
import com.example.demo.api.coupon.app.CouponRedeemService;
import com.example.demo.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponRedeemApi {
    private final CouponRedeemService couponRedeemService;

    @PostMapping("/api/v1/coupons/{couponId}/redeem")
    public ApiResponse<Void> redeemCoupon(@PathVariable Integer couponId, User user) {
        try {
            couponRedeemService.redeemCoupon(new CouponRedeemRequest(user.getId(), couponId));
        } catch (NoDataException e) {
            return ApiResponse.badRequest(e.getMessage());
        }

        return ApiResponse.ok();
    }
}
