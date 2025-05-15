package com.example.demo.api.coupon.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class CouponIssueWrapper {
    private final CouponIssueService couponIssueService;
    private final ReentrantLock lock = new ReentrantLock();

    public void issueCouponWithLock(CouponIssueRequest req) throws InterruptedException {
        if (!lock.tryLock(1,TimeUnit.MINUTES)) {
            throw new InterruptedException();
        }

        try {
            couponIssueService.issueCoupon(req);
        } finally {
            lock.unlock();
        }
    }
}
