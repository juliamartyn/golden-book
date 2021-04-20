package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.CouponRequest;
import com.juliamartyn.goldenbook.controllers.response.CouponResponse;
import com.juliamartyn.goldenbook.entities.Coupon;
import com.juliamartyn.goldenbook.repository.CouponRepository;
import com.juliamartyn.goldenbook.repository.OrderRepository;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.services.CouponService;
import com.juliamartyn.goldenbook.services.converters.CouponConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponConverter couponConverter;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CouponServiceImpl(CouponRepository couponRepository, CouponConverter couponConverter,
                             UserRepository userRepository, OrderRepository orderRepository) {
        this.couponRepository = couponRepository;
        this.couponConverter = couponConverter;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void create(CouponRequest couponRequest) {
        couponRequest.getCustomersId().forEach(customerId -> {
            Coupon coupon = Coupon.builder()
                    .discount(couponRequest.getDiscount())
                    .dueDate(couponRequest.getDueDate())
                    .bookQuantity(couponRequest.getBookQuantity())
                    .build();
            coupon.setCustomer(userRepository.findUserById(customerId));
            couponRepository.save(coupon);
        });
    }

    @Override
    public List<CouponResponse> findUnusedByCustomerId(Long customerId) {
        return couponRepository.findUnusedByCustomerId(customerId).stream()
                .map(couponConverter::of)
                .collect(Collectors.toList());
    }
}
