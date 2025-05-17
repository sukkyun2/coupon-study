package com.example.demo.api.coupon.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponHistoryRepository extends JpaRepository<CouponHistory,CouponHistoryId> {
}
