package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.EmailHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Integer> {

    EmailHistory findEmailHistoryById(Integer id);

    Page<EmailHistory> findAll(Pageable pageable);
}
