package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Order;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.OrderRepository;
import com.juliamartyn.goldenbook.repository.OrderStatusRepository;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.OrderService;
import com.juliamartyn.goldenbook.services.converters.OrderConverter;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final BookRepository bookRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final MailSender mailSender;

    public OrderServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter, BookRepository bookRepository,
                            OrderStatusRepository orderStatusRepository, UserRepository userRepository, MailSender mailSender) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.bookRepository = bookRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public OrderResponse addBook(Integer bookId, Long currentUserId) {
        Book newBook = bookRepository.findBookById(bookId);
        Order order = orderRepository.findOrderWithStatusSavedByBuyerId(currentUserId);

        if(order != null) {
            List<Book> bookList = new ArrayList<>(order.getBooks());
            bookList.add(newBook);
            order.setBooks(bookList);
            order.setTotalPrice(order.getTotalPrice().add(newBook.getPrice()));

            return orderConverter.of(orderRepository.save(order));
        } else {
            Order newOrder = createOrderForCurrentUser(currentUserId);
            List<Book> bookList = new ArrayList<>();
            bookList.add(newBook);
            newOrder.setBooks(bookList);
            newOrder.setTotalPrice(newBook.getPrice());

            return orderConverter.of(orderRepository.save(newOrder));
        }
    }

    @Override
    public void updateStatus(Integer id, String status) throws MessagingException {
        Integer statusId = orderStatusRepository.findByName(status).getId();
        if(orderRepository.updateStatus(id, statusId) == 0){
            throw new NotFoundException("Order with id " + id + "not found");
        }

        sendOrderEmail(id, MailSenderImpl.MailType.ORDER_STATUS_UPDATE);
    }

    @Override
    public OrderResponse findOrderWithStatusSavedByBuyerId(Long id){
        return orderConverter.of(orderRepository.findOrderWithStatusSavedByBuyerId(id));
    }

    @Override
    public List<OrderResponse> findAllConfirmedOrders() {
        return orderRepository.findAllConfirmedOrders().stream()
                .map(orderConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> findOrdersByBuyerId(Long buyerId) {
        return orderRepository.findAllConfirmedOrdersByBuyerId(buyerId).stream()
                .map(orderConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBookFromCart(Integer orderId, Integer bookId){
        Order order = orderRepository.findOrderById(orderId);
        Book bookToDelete = bookRepository.findBookById(bookId);

        order.getBooks().remove(bookToDelete);
        orderRepository.updateTotalPrice(orderId, order.getTotalPrice().subtract(bookToDelete.getPrice()));

        orderRepository.save(order);
    }

    @Override
    public void confirmOrder(Integer orderId) throws MessagingException {
        changeOrderStatusToORDERED(orderId);

        orderRepository.findOrderById(orderId).getBooks()
                .forEach(book -> bookRepository.updateQuantity(book.getId(), book.getQuantity() - 1));

        sendOrderEmail(orderId, MailSenderImpl.MailType.ORDER_CONFIRMED);
    }

    @Override
    public void preOrder(Integer bookId, Long currentUserId) {
        Order preOrder = new Order();
        preOrder.setStatus(orderStatusRepository.findByName("PREORDERED"));
        preOrder.setBuyer(userRepository.findUserById(currentUserId));

        Book book = bookRepository.findBookById(bookId);
        preOrder.setBooks(List.of(book));
        preOrder.setTotalPrice(book.getPrice());
        bookRepository.updateQuantity(bookId, book.getQuantity() - 1);

        orderRepository.save(preOrder);
    }

    @Override
    public List<OrderResponse> findPreOrdersByBuyerId(Long buyerId) {
        return orderRepository.findPreOrdersByBuyerId(buyerId).stream()
                .map(orderConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelPreOrder(Integer orderId) {
        Order order = orderRepository.findOrderById(orderId);
        if(order.getStatus().getName().equals("PREORDERED")) {
            order.getBooks().forEach(book -> bookRepository.updateQuantity(book.getId(), book.getQuantity() + 1));
            orderRepository.deleteById(orderId);
        }
    }

    @Override
    public void confirmPreOrder(Integer orderId) throws MessagingException {
        changeOrderStatusToORDERED(orderId);

        sendOrderEmail(orderId, MailSenderImpl.MailType.ORDER_CONFIRMED);
    }

    private Order createOrderForCurrentUser(Long currentUserId) {
        Order newOrder = new Order();
        newOrder.setStatus(orderStatusRepository.findByName("SAVED"));
        newOrder.setBuyer(userRepository.findUserById(currentUserId));

        return orderRepository.save(newOrder);
    }

    private void changeOrderStatusToORDERED(Integer orderId) {
        Integer statusId = orderStatusRepository.findByName("ORDERED").getId();
        orderRepository.updateStatus(orderId, statusId);
    }

    private void sendOrderEmail(Integer orderId, MailSenderImpl.MailType mailType) throws MessagingException {
        Order order = orderRepository.findOrderById(orderId);

        Map<String, Object> mailContext = new HashMap<>();
        mailContext.put("orderId", orderId);
        mailContext.put("status", order.getStatus().getName());
        mailContext.put("username", order.getBuyer().getUsername());

        mailSender.sendEmail(order.getBuyer().getEmail(), "GoldenBook order", mailType, mailContext);
    }
}
