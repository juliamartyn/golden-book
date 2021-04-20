package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    private Integer id;
    private Integer discount;
    private LocalDate dueDate;
    private Integer bookQuantity;
    private Long customerId;
}
