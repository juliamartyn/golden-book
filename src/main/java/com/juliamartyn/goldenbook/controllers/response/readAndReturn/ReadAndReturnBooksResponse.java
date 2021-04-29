package com.juliamartyn.goldenbook.controllers.response.readAndReturn;

import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadAndReturnBooksResponse {
    private Integer id;
    private BookResponse book;
    private BigDecimal pricePerDay;
}
