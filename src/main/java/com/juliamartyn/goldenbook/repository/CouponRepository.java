package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    @Query(value = "select * from coupons " +
                   "where coupons.used = 'false' and coupons.customer_id = :customerId", nativeQuery = true)
    List<Coupon> findUnusedByCustomerId(Long customerId);
    Coupon findCouponById(Integer id);
}
