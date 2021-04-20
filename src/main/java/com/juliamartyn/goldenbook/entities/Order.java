package com.juliamartyn.goldenbook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "status_id",  referencedColumnName = "id")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "buyer_id",  referencedColumnName = "id")
    private User buyer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_books",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books;

    @OneToOne
    @JoinColumn(name = "coupon_id",  referencedColumnName = "id")
    private Coupon coupon;
}
