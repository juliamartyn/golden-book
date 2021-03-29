package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Integer id;
    private String status;
    private BigDecimal totalPrice;
    private UserResponse buyer;
    private List<BookResponse> books;
}
