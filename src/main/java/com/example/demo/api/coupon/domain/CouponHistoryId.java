package com.example.demo.api.coupon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CouponHistoryId implements Serializable {

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "COUPON_ID")
    private Integer couponId;
}
