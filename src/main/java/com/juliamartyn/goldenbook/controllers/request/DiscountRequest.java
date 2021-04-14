package com.juliamartyn.goldenbook.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    private Integer discount;
    private LocalDate dueDate;
    private List<Integer> booksId;
}
