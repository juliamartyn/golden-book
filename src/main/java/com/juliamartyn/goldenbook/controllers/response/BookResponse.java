package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private byte[] image;
}
