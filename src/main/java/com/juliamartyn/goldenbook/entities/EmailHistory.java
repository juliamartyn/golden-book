package com.juliamartyn.goldenbook.entities;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "email_history")
public class EmailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private emailType emailType;

    @OneToOne
    @JoinColumn(name = "order_id",  referencedColumnName = "id")
    private Order order;

    public enum emailType{
        ORDER_STATUS_UPDATE,
        ORDER_CONFIRMED,
        BOOK_AVAILABLE,
        LAST_RENT_DAY,
        RENT_DATE_REMINDER
    }

}
