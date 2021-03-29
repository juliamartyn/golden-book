package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    OrderStatus findByName(String statusName);
}
