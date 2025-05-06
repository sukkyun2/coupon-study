package com.example.demo.api.coupon.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CouponCreateValidator {
    private static final String ERROR_NAME_REQUIRED = "쿠폰 이름은 필수입니다.";
    private static final String ERROR_AMOUNT_INVALID = "쿠폰 수량은 0보다 커야 합니다.";
    private static final String ERROR_TYPE_REQUIRED = "쿠폰 타입은 필수입니다.";
    private static final String ERROR_EXPIRED_AT_REQUIRED = "쿠폰 만료일은 필수입니다.";
    private static final String ERROR_EXPIRED_AT_INVALID = "쿠폰 만료일은 현재 시각 이후여야 합니다.";

    public List<String> validate(CouponCreateRequest req) {
        List<String> errors = new ArrayList<>();

        if (req.name() == null || req.name().trim().isEmpty()) {
            errors.add(ERROR_NAME_REQUIRED);
        }

        if (req.amount() <= 0) {
            errors.add(ERROR_AMOUNT_INVALID);
        }

        if (req.couponType() == null) {
            errors.add(ERROR_TYPE_REQUIRED);
        }

        if (req.expiredAt() == null) {
            errors.add(ERROR_EXPIRED_AT_REQUIRED);
        } else if (req.expiredAt().isBefore(LocalDateTime.now())) {
            errors.add(ERROR_EXPIRED_AT_INVALID);
        }

        return errors;
    }
}
