package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
}
