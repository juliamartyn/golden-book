package com.juliamartyn.goldenbook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer discount;

    private LocalDate dueDate;

    private Integer bookQuantity;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Builder.Default
    private Boolean used = false;

    @ManyToOne
    @JoinColumn(name = "customer_id",  referencedColumnName = "id")
    private User customer;

    public enum CouponType{
        PERSONAL,
        SHARED
    }
}
