package com.example.demo.api.coupon.app;

import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.coupon.domain.*;
import com.example.demo.api.coupon.domain.enums.CouponType;
import com.example.demo.api.user.domain.User;
import com.example.demo.api.user.domain.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponIssueServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private CouponIssueService couponIssueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("정상 발급 시 수량 차감되고 발급이력 저장됨")
    void 쿠폰_발급_성공() {
        // given
        Integer couponId = 1;
        Integer userId = 10;
        CouponIssueRequest request = new CouponIssueRequest(userId, couponId);

        Coupon coupon = new Coupon(couponId, "쿠폰명", 100, 100, CouponType.CHICKEN, LocalDateTime.now().plusHours(1), LocalDateTime.now(),0);
        User user = new User(userId, "유저명");

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(userQueryService.getUser(userId)).thenReturn(user);
        when(couponHistoryRepository.existsById(any())).thenReturn(false);

        // when
        couponIssueService.issueCoupon(request);

        // then
        assertEquals(99, coupon.getQuantity());
        verify(couponHistoryRepository).save(any(CouponHistory.class));
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰 발급시 예외가 발생한다")
    void 쿠폰_없음() {
        // given
        Integer couponId = 1;
        Integer userId = 10;
        CouponIssueRequest request = new CouponIssueRequest(userId, couponId);

        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        // expect
        assertThrows(NoDataException.class, () -> couponIssueService.issueCoupon(request));
    }

    @Test
    @DisplayName("이미 발급된 쿠폰을 중복 발급하려 하면 실패한다")
    void 쿠폰_중복_발급() {
        // given
        Integer couponId = 1;
        Integer userId = 10;
        CouponIssueRequest request = new CouponIssueRequest(userId, couponId);

        Coupon coupon = new Coupon(couponId, "쿠폰명", 100, 100, CouponType.CHICKEN, LocalDateTime.now().plusHours(1), LocalDateTime.now(),0);
        User user = new User(userId, "유저명");

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(userQueryService.getUser(userId)).thenReturn(user);
        when(couponHistoryRepository.existsById(any())).thenReturn(true);

        // expect
        assertThrows(CouponAlreadyExistException.class, () -> couponIssueService.issueCoupon(request));
    }
}
