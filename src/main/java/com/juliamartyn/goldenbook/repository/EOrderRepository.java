package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.EOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EOrderRepository extends JpaRepository<EOrder, Integer> {
    @Query(value = "select ebooks.file_reference from ebooks " +
            "join books b on b.ebook_id = ebooks.id " +
            "join orders_books ob on ob.book_id = b.id " +
            "join orders o on o.id = ob.order_id " +
            "join e_orders eo on eo.order_id = o.id " +
            "where eo.code = :code", nativeQuery = true)
    String findFileReferenceByEOrderCode(String code);
}
