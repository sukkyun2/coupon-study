package com.example.demo.api.coupon.app;

import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.coupon.domain.*;
import com.example.demo.api.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponRedeemService {
    private final CouponHistoryRepository couponHistoryRepository;

    @Transactional
    public void redeemCoupon(CouponRedeemRequest req){
        CouponHistory history = getCouponHistory(req.couponId(), req.userId());

        history.use();

        couponHistoryRepository.save(history);
    }

    private CouponHistory getCouponHistory(Integer couponId, Integer userId){
        return couponHistoryRepository.findById(new CouponHistoryId(couponId, userId))
                .orElseThrow(NoDataException::new);
    }
}
