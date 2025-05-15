package com.example.demo.api.coupon.app;

import com.example.demo.api.coupon.domain.Coupon;
import com.example.demo.api.coupon.domain.CouponHistoryRepository;
import com.example.demo.api.coupon.domain.CouponRepository;
import com.example.demo.api.coupon.domain.enums.CouponType;
import com.example.demo.api.user.domain.User;
import com.example.demo.api.user.domain.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrentCouponIssueServiceTest {

    private static final int THREAD_COUNT = 10000;
    private static final int COUPON_AMOUNT = 10000;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponHistoryRepository couponHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponIssueWrapper couponIssueWrapper;

    private int couponId;

    @BeforeEach
    void setUp() {
        Coupon coupon = new Coupon("TEST_COUPON", COUPON_AMOUNT, CouponType.CHICKEN, LocalDateTime.now().plusHours(5));
        couponId = couponRepository.save(coupon).getCouponId();

        for (int i = 1; i <= THREAD_COUNT; i++) {
            userRepository.save(createUser(i));
        }
    }

    @AfterEach
    void cleanUp() {
        couponHistoryRepository.deleteAll();
        couponRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User createUser(int id) {
        return new User(id, "이름" + id);
    }

    @Test
    @DisplayName("10000명의 사용자에게 한장씩 쿠폰이 발급되어야 한다")
    void preventDuplicateAndOverIssueInConcurrency() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            final int userId = i;
            executorService.submit(() -> {
                try {
                    couponIssueWrapper.issueCouponWithLock(new CouponIssueRequest(userId, couponId));
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon coupon = couponRepository.findById(couponId).orElseThrow();
        assertEquals(0, coupon.getQuantity(), "쿠폰 수량은 0이어야 한다");
    }
}

