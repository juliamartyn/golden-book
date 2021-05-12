package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Integer> {
}
