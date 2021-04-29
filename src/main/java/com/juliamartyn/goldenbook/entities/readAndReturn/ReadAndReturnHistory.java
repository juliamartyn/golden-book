package com.juliamartyn.goldenbook.entities.readreturn;


import com.juliamartyn.goldenbook.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "read_return_history")
public class ReadAndReturnHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt;

    private Integer rentDaysNumber;

    private LocalDate expectedReturnDate;
    @ManyToOne
    @JoinColumn(name = "customer_id",  referencedColumnName = "id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "tariff_id",  referencedColumnName = "id")
    private ReadAndReturnTariff tariff;
}
