package com.juliamartyn.goldenbook.services;


import com.juliamartyn.goldenbook.controllers.response.OrderResponse;

import javax.mail.MessagingException;
import java.util.List;

public interface OrderService {
    OrderResponse addBook(Integer bookId, Long currentUserId);
    void updateStatus(Integer id, String status) throws MessagingException;
    OrderResponse findOrderWithStatusSavedByBuyerId(Long id);
    List<OrderResponse> findAllConfirmedOrders();
    List<OrderResponse> findOrdersByBuyerId(Long buyerId);
    void deleteBookFromCart(Integer orderId, Integer bookId);
    void confirmOrder(Integer orderId) throws MessagingException;
    void preOrder(Integer bookId, Long currentUserId);
    List<OrderResponse> findPreOrdersByBuyerId(Long id);
    void cancelPreOrder(Integer orderId);
    void confirmPreOrder(Integer orderId) throws MessagingException;
}
