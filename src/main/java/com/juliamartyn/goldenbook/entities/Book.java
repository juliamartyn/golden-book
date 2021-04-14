package com.juliamartyn.goldenbook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String author;

    private byte[] image;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private LocalDate startSaleDate;

    @ManyToOne
    @JoinColumn(name = "category_id",  referencedColumnName = "id")
    private BookCategory category;

    @ManyToOne
    @JoinColumn(name = "discount_id",  referencedColumnName = "id")
    private Discount discount;

    private BigDecimal priceWithDiscount;
}
