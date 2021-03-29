package com.juliamartyn.goldenbook.services;


import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse addBook(Integer bookId, Long currentUserId);
    void updateStatus(Integer id, String status);
    OrderResponse findOrderWithStatusSavedByBuyerId(Long id);
    List<OrderResponse> findAllConfirmedOrders();
    List<OrderResponse> findOrdersByBuyerId(Long buyerId);
    void deleteBookFromCart(Integer orderId, Integer bookId);
    void confirmOrder(Integer orderId);
}
