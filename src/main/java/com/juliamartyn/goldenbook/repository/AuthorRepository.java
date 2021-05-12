package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findAuthorByNameAndSurname(String name, String surname);
}
