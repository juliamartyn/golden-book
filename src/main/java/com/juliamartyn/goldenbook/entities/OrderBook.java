package com.juliamartyn.goldenbook.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "orders_books")
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookType bookType;

    @ManyToOne
    @JoinColumn(name = "book_id",  referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id",  referencedColumnName = "id")
    private Order order;

    public enum BookType{
        PAPER,
        ELECTRONIC
    }
}
