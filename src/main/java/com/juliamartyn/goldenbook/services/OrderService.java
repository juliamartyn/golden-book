package com.juliamartyn.goldenbook.services;


import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import net.sf.jasperreports.engine.JRException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.List;

public interface OrderService {
    OrderResponse addBook(Integer bookId, Long currentUserId);
    void updateStatus(Integer id, String status) throws MessagingException;
    OrderResponse findOrderWithStatusSavedByBuyerId(Long id);
    List<OrderResponse> findAllConfirmedOrders();
    List<OrderResponse> findOrdersByBuyerId(Long buyerId);
    void deleteBookFromCart(Integer orderId, Integer bookId);
    void confirmOrder(Integer orderId) throws MessagingException, FileNotFoundException, JRException;
    void preOrder(Integer bookId, Long currentUserId);
    void orderEBook(Integer bookId, Long currentUserId) throws FileNotFoundException, JRException, MessagingException;
    List<OrderResponse> findPreOrdersByBuyerId(Long id);
    void cancelPreOrder(Integer orderId);
    void confirmPreOrder(Integer orderId) throws MessagingException, FileNotFoundException, JRException;
    void applyCouponToOrder(Integer orderId, Integer couponId);
}
