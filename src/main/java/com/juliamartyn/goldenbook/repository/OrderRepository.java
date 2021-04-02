package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from orders where status_id = 1 and buyer_id = :id", nativeQuery = true)
    Order findOrderWithStatusSavedByBuyerId(Long id);

    @Query(value = "select * from orders where status_id != 1", nativeQuery = true)
    List<Order> findAllConfirmedOrders();

    @Query(value = "select * from orders where status_id != 1 and buyer_id = :id", nativeQuery = true)
    List<Order> findAllConfirmedOrdersByBuyerId(Long id);

    @Query(value = "select * from orders where status_id = 5 and buyer_id = :id", nativeQuery = true)
    List<Order> findPreOrdersByBuyerId(Long id);

    Order findOrderById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update orders o set o.status_id = :status where o.id = :id", nativeQuery = true)
    int updateStatus(Integer id, Integer status);

    @Modifying
    @Transactional
    @Query(value = "update orders o set o.total_price = :price where o.id = :id", nativeQuery = true)
    int updateTotalPrice(Integer id, BigDecimal price);


    @Query(value = "select * from orders " +
            "join orders_books on orders.id = orders_books.order_id " +
            "join books on orders_books.book_id = books.id " +
            "where orders.status_id = 5 and books.start_sale_date = CURDATE()", nativeQuery = true)
    List<Order> findAllPreOrdersWithStartSellingTodayBooks();

}
