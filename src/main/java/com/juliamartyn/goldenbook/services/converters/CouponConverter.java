package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.request.CouponRequest;
import com.juliamartyn.goldenbook.controllers.response.CouponResponse;
import com.juliamartyn.goldenbook.entities.Coupon;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CouponConverter {

    private final ModelMapper modelMapper;

    public Coupon of(CouponRequest couponRequest) {
        return modelMapper.map(couponRequest, Coupon.class);
    }

    public CouponResponse of(Coupon coupon){
        return  modelMapper.map(coupon, CouponResponse.class);
    }
}
