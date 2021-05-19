package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.EBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EBookRepository extends JpaRepository<EBook, Integer> {
}
