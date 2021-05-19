package com.juliamartyn.goldenbook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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

    private byte[] image;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private LocalDate startSaleDate;

    @ManyToOne
    @JoinColumn(name = "author_id",  referencedColumnName = "id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id",  referencedColumnName = "id")
    private BookCategory category;

    @ManyToOne
    @JoinColumn(name = "discount_id",  referencedColumnName = "id")
    private Discount discount;

    private BigDecimal priceWithDiscount;

    @OneToOne
    @JoinColumn(name = "ebook_id",  referencedColumnName = "id")
    private EBook ebook;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<OrderBook> orderBooks;
}
