package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.CouponRequest;
import com.juliamartyn.goldenbook.controllers.response.CouponResponse;
import com.juliamartyn.goldenbook.security.services.UserPrinciple;
import com.juliamartyn.goldenbook.services.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping
    public ResponseEntity<Void> createCoupon(@RequestBody CouponRequest couponRequest) {
        couponService.create(couponRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/current-user")
    public ResponseEntity<List<CouponResponse>> findUnusedByCustomerId(Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        return new ResponseEntity<>(couponService.findUnusedByCustomerId(currentUser.getId()), HttpStatus.OK);
    }
}
