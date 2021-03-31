package com.juliamartyn.goldenbook.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private LocalDateTime startSaleDate;

    @ManyToOne
    @JoinColumn(name = "category_id",  referencedColumnName = "id")
    private BookCategory category;

}
