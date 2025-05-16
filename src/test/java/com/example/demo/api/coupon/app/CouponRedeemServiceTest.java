package com.example.demo.api.coupon.app;

import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.coupon.domain.Coupon;
import com.example.demo.api.coupon.domain.CouponHistory;
import com.example.demo.api.coupon.domain.CouponHistoryId;
import com.example.demo.api.coupon.domain.CouponHistoryRepository;
import com.example.demo.api.coupon.domain.enums.CouponType;
import com.example.demo.api.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponRedeemServiceTest {

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @InjectMocks
    private CouponRedeemService couponRedeemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("쿠폰을 정상적으로 사용할 수 있다")
    void 쿠폰사용_성공() {
        // given
        CouponHistory history = createHistory();
        CouponRedeemRequest givenRequest = new CouponRedeemRequest(history.getUser().getId(), history.getCoupon().getCouponId());

        when(couponHistoryRepository.findById(any())).thenReturn(Optional.of(history));

        // when
        couponRedeemService.redeemCoupon(givenRequest);

        // then
        assertTrue(history.getUse());
        assertNotNull(history.getUsedAt());
        verify(couponHistoryRepository).save(history);
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰 사용 시 예외가 발생한다")
    void 쿠폰사용_실패_쿠폰없음() {
        // given
        CouponRedeemRequest givenRequest = new CouponRedeemRequest(1, 1);
        when(couponHistoryRepository.findById(any())).thenReturn(Optional.empty());

        // expect
        assertThrows(NoDataException.class, () -> couponRedeemService.redeemCoupon(givenRequest));
    }

    private CouponHistory createHistory() {
        User user = new User(1, "테스트 유저");
        Coupon coupon = new Coupon(100, "테스트 쿠폰", 100, 100,
                CouponType.CHICKEN, LocalDateTime.now().plusDays(1), LocalDateTime.now(),0);
        return new CouponHistory(new CouponHistoryId(coupon.getCouponId(), user.getId()), user, coupon);
    }
}
