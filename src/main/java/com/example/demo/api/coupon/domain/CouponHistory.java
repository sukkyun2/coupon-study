package com.example.demo.api.coupon.domain;

import com.example.demo.api.common.infra.BooleanYNConverter;
import com.example.demo.api.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_COUPON_HIST")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponHistory {

    @EmbeddedId
    private CouponHistoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("couponId")
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    @Convert(converter = BooleanYNConverter.class)
    @Column(name = "USED_YN")
    private Boolean use;

    @Column(name = "USED_AT")
    private LocalDateTime usedAt;

    public CouponHistory(CouponHistoryId id, User user, Coupon coupon) {
        assert user != null;
        assert coupon != null;

        this.id = id;
        this.user = user;
        this.coupon = coupon;
        this.use = false;
    }

    public void use() {
        if (!isAvailable()) {
            throw new CouponUnavailableException();
        }

        this.use = true;
        this.usedAt = LocalDateTime.now();
    }

    private boolean isAvailable() {
        return LocalDateTime.now().isBefore(coupon.getExpiredAt()) && !use;
    }
}
