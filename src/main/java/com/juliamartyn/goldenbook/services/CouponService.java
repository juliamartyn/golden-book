package com.juliamartyn.goldenbook.services;

import com.juliamartyn.goldenbook.controllers.request.CouponRequest;
import com.juliamartyn.goldenbook.controllers.response.CouponResponse;

import java.util.List;

public interface CouponService {
    void create(CouponRequest couponRequest);
    List<CouponResponse> findUnusedByCustomerId(Long customerId);
}
