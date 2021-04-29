package com.juliamartyn.goldenbook.entities.readAndReturn;

import com.juliamartyn.goldenbook.entities.Book;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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

    private BigDecimal pricePerDay;

    @OneToOne
    @JoinColumn(name = "book_id",  referencedColumnName = "id")
    private Book book;

    @OneToMany(mappedBy = "tariff")
    private Set<ReadAndReturnHistory> history;
}
