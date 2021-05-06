package com.juliamartyn.goldenbook.services;


import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.request.DiscountRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    BookResponse create(BookRequest bookRequest, MultipartFile file) throws MessagingException;
    void updateQuantity(Integer id, Integer quantity);
    void updatePrice(Integer id, BigDecimal price);
    void delete(Integer id);
    List<BookResponse> findAll();
    BookPageableResponse findPageableBooks(int pageNo, int pageSize);
    BookResponse findBookById(Integer id);
    List<BookResponse> findTopSellingBooksByCategory(Integer category);
    void addDiscount(DiscountRequest discountRequest);
}
