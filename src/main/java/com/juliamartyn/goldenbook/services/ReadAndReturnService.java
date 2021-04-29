package com.juliamartyn.goldenbook.services;

import com.juliamartyn.goldenbook.controllers.request.readAndReturn.ReadAndReturnCreateRequest;
import com.juliamartyn.goldenbook.controllers.request.readAndReturn.RentBookRequest;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.ReadAndReturnBooksResponse;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.RentedBooksResponse;
import net.sf.jasperreports.engine.JRException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface ReadAndReturnService {
    void create(ReadAndReturnCreateRequest request);
    void updatePricePerDay(Integer id, BigDecimal pricePerDay);
    List<ReadAndReturnBooksResponse> findAllReadAndReturnBooks();
    RentedBooksResponse rentBook(RentBookRequest request, Long currentUserId) throws FileNotFoundException, JRException, MessagingException;
    List<RentedBooksResponse> findAllRentedBooks();
    void updateEmailReminding(Integer id, boolean emailRemaining);
}
