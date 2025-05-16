package com.example.demo.api.coupon.domain;

import com.example.demo.api.coupon.domain.enums.CouponType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "COUPON")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUPON_ID")
    private Integer couponId;

    @Column(name = "COUPON_NM")
    private String couponName;

    @Column(name = "AMOUNT")
    private Integer amount;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "COUPON_TYPE")
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(name = "EXPIRED_AT")
    private LocalDateTime expiredAt;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Version
    private long version;

    @Builder
    public Coupon(String couponName, Integer amount, CouponType couponType, LocalDateTime expiredAt) {
        this.couponName = couponName;
        this.amount = amount;
        this.quantity = amount;
        this.couponType = couponType;
        this.expiredAt = expiredAt;
        this.createdAt = LocalDateTime.now();
        this.version = 0;
    }

    public boolean isQuantityAvailable(){
        return quantity > 0;
    }

    public void decreaseQuantity(){
        if(!isQuantityAvailable()){
            throw new NoQuantityException();
        }

        quantity -= 1;
    }
}

