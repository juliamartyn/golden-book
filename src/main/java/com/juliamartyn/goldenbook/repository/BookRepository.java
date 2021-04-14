package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Discount;
import com.juliamartyn.goldenbook.entities.EmailHistory;
import com.juliamartyn.goldenbook.services.impl.reports.SoldBooksReportValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update books b set b.quantity = :quantity where b.id = :id", nativeQuery = true)
    int updateQuantity(Integer id, Integer quantity);

    @Modifying
    @Transactional
    @Query(value = "update books b set b.price = :price where b.id = :id", nativeQuery = true)
    int updatePrice(Integer id, BigDecimal price);

    Book findBookById(Integer bookId);

    @Query(value = "select books.* from books " +
            "join orders_books on books.id = orders_books.book_id " +
            "join orders on orders_books.order_id = orders.id " +
            "where books.category_id = :categoryId and orders.status_id != 1 " +
            "group by orders_books.book_id " +
            "order by count(orders_books.book_id) DESC " +
            "limit 3", nativeQuery = true)
    List<Book> findTopSellingBooksByCategoryId(Integer categoryId);


    @Query(value = "select  count(books.id) as quantity, sum(books.price) as amount from books " +
            "join orders_books on books.id = orders_books.book_id " +
            "join orders on orders_books.order_id = orders.id " +
            "where orders.status_id != 1 and books.category_id = :categoryId " +
            "and orders.created_at between :startDate and :endDate", nativeQuery = true)
    SoldBooksReportValues getTotalValuesOfSoldBooksByCategory(Integer categoryId, String startDate, String endDate);

    Page<Book> findAll(Pageable pageable);

    List<Book> findBooksByDiscountId(Integer discountId);
}
