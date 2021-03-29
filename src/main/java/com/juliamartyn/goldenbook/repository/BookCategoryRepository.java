package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory,Integer> {
    BookCategory findByName(String categoryName);
}
