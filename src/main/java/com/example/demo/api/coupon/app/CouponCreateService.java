package com.example.demo.api.coupon.app;

import com.example.demo.api.common.app.ValidationException;
import com.example.demo.api.coupon.domain.Coupon;
import com.example.demo.api.coupon.domain.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponCreateService {
    private final CouponCreateValidator validator;
    private final CouponRepository couponRepository;

    public CouponCreateService(CouponRepository couponRepository) {
        this.validator = new CouponCreateValidator();
        this.couponRepository = couponRepository;
    }

    public void createCoupon(CouponCreateRequest req) {
        List<String> errors = validator.validate(req);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.get(0));
        }

        Coupon coupon = convert(req);

        couponRepository.save(coupon);
    }

    private Coupon convert(CouponCreateRequest req) {
        return new Coupon(
                req.name(),
                req.amount(),
                req.couponType(),
                req.expiredAt()
        );
    }
}
