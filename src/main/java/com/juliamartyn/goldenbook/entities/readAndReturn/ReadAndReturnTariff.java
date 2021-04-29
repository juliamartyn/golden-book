package com.juliamartyn.goldenbook.entities.readreturn;

import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "read_return_tariff")
public class ReadAndReturnTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal pricePerDate;

    @OneToOne
    @JoinColumn(name = "book_id",  referencedColumnName = "id")
    private Book book;

    @OneToMany(mappedBy = "tariff")
    private Set<ReadAndReturnHistory> history;


}
