package com.example.demo.api.coupon.app;

import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.common.infra.DistributedLock;
import com.example.demo.api.coupon.domain.*;
import com.example.demo.api.user.domain.User;
import com.example.demo.api.user.domain.UserQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final UserQueryService userQueryService;

    @Transactional
    @DistributedLock //FIXME key값 메서드명 + couponId로 변경
    public void issueCoupon(CouponIssueRequest req) {
        Coupon coupon = couponRepository.findById(req.couponId()).orElseThrow(NoDataException::new);
        User user = userQueryService.getUser(req.userId());

        if (hasIssuedCoupon(user, coupon)) {
            throw new CouponAlreadyExistException();
        }

        coupon.decreaseQuantity();

        couponRepository.save(coupon);
        appendHistory(user, coupon);
    }

    private boolean hasIssuedCoupon(User user, Coupon coupon) {
        CouponHistoryId couponHistoryId = new CouponHistoryId(user.getId(), coupon.getCouponId());

        return couponHistoryRepository.existsById(couponHistoryId);
    }

    private void appendHistory(User user, Coupon coupon) {
        CouponHistoryId couponHistoryId = new CouponHistoryId(user.getId(), coupon.getCouponId());

        CouponHistory history = new CouponHistory(couponHistoryId, user, coupon);
        couponHistoryRepository.save(history);
    }
}
