package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Integer id;
    private String title;
    private String author;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private LocalDate startSaleDate;
    private byte[] image;
    private BigDecimal priceWithDiscount;
    private Integer ebookId;
    private BigDecimal ebookPrice;
}
