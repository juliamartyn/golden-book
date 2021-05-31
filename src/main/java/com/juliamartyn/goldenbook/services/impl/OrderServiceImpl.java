package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Coupon;
import com.juliamartyn.goldenbook.entities.Order;
import com.juliamartyn.goldenbook.entities.OrderBook;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.CouponRepository;
import com.juliamartyn.goldenbook.repository.OrderBookRepository;
import com.juliamartyn.goldenbook.repository.OrderRepository;
import com.juliamartyn.goldenbook.repository.OrderStatusRepository;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.OrderService;
import com.juliamartyn.goldenbook.services.converters.OrderConverter;
import com.juliamartyn.goldenbook.utils.PdfGenerator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${INVOICE_REPOSITORY}")
    private String invoiceRepository;

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final BookRepository bookRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final MailSender mailSender;
    private final PdfGenerator pdfGenerator;
    private final OrderBookRepository orderBookRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter,
                            BookRepository bookRepository, OrderStatusRepository orderStatusRepository,
                            UserRepository userRepository, CouponRepository couponRepository,
                            MailSender mailSender, PdfGenerator pdfGenerator, OrderBookRepository orderBookRepository) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.bookRepository = bookRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.mailSender = mailSender;
        this.pdfGenerator = pdfGenerator;
        this.orderBookRepository = orderBookRepository;
    }

    @Override
    public OrderResponse addBook(Integer bookId, Long currentUserId) {
        Book newBook = bookRepository.findBookById(bookId);
        Order order = orderRepository.findOrderWithStatusSavedByBuyerId(currentUserId);

        if(order != null) {
            Set<OrderBook> orderBookSet = new HashSet<>(order.getOrderBooks());

            OrderBook orderBook = OrderBook.builder()
                    .book(newBook)
                    .order(order)
                    .bookType(OrderBook.BookType.PAPER)
                    .build();
            orderBookRepository.save(orderBook);
            orderBookSet.add(orderBook);
            order.setOrderBooks(orderBookSet);
            order.setTotalPrice(order.getTotalPrice().add(newBook.getPriceWithDiscount()));

            return orderConverter.of(orderRepository.save(order));
        } else {
            Order newOrder = createOrderForCurrentUser(currentUserId);
            Set<OrderBook> orderBookSet = new HashSet<>();

            OrderBook orderBook = OrderBook.builder()
                    .book(newBook)
                    .order(newOrder)
                    .bookType(OrderBook.BookType.PAPER)
                    .build();
            orderBookRepository.save(orderBook);

            orderBookSet.add(orderBook);
            newOrder.setOrderBooks(orderBookSet);
            newOrder.setTotalPrice(newBook.getPriceWithDiscount());

            return orderConverter.of(orderRepository.save(newOrder));
        }
    }

    @Override
    public void updateStatus(Integer id, String status) throws MessagingException {
        Integer statusId = orderStatusRepository.findByName(status).getId();
        if(orderRepository.updateStatus(id, statusId) == 0){
            throw new NotFoundException("Order with id " + id + "not found");
        }

        sendOrderStatusUpdatedEmail(id);
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

        for (OrderBook orderBook : order.getOrderBooks()) {
            if (orderBook.getBook().getId().equals(bookId)) {
                orderBookRepository.delete(orderBook);
                order.getOrderBooks().remove(orderBook);
            }
        }
        orderRepository.updateTotalPrice(orderId, order.getTotalPrice().subtract(bookToDelete.getPrice()));

        orderRepository.save(order);
    }

    @Override
    public void confirmOrder(Integer orderId) throws MessagingException, FileNotFoundException, JRException {
        changeOrderStatusToORDERED(orderId);

        orderRepository.findOrderById(orderId).getOrderBooks().forEach(orderBook ->
                bookRepository.updateQuantity(orderBook.getBook().getId(), orderBook.getBook().getQuantity() - 1));

        sendOrderConfirmationEmail(orderId, generateInvoice(orderId));
    }

    @Override
    public void preOrder(Integer bookId, Long currentUserId) {
        Order preOrder = new Order();
        preOrder.setStatus(orderStatusRepository.findByName("PREORDERED"));
        preOrder.setBuyer(userRepository.findUserById(currentUserId));

        Book book = bookRepository.findBookById(bookId);
        preOrder.setTotalPrice(book.getPrice());
        bookRepository.updateQuantity(bookId, book.getQuantity() - 1);

        orderRepository.save(preOrder);

        OrderBook orderBook = OrderBook.builder()
                .book(book)
                .order(preOrder)
                .bookType(OrderBook.BookType.PAPER)
                .build();
        orderBookRepository.save(orderBook);
    }

    @Override
    public void orderEBook(Integer bookId, Long currentUserId) throws FileNotFoundException, JRException, MessagingException {
        Book book = bookRepository.findBookById(bookId);
        Order order = Order.builder().status(orderStatusRepository.findByName("E_ORDERED"))
                .buyer(userRepository.findUserById(currentUserId))
                .totalPrice(book.getEbook().getPrice())
                .build();
        orderRepository.save(order);

        OrderBook orderBook = OrderBook.builder()
                .book(book)
                .order(order)
                .bookType(OrderBook.BookType.ELECTRONIC)
                .build();
        orderBookRepository.save(orderBook);

        Set<OrderBook> orderBookSet = new HashSet<>();
        orderBookSet.add(orderBook);
        order.setOrderBooks(orderBookSet);
        orderRepository.save(order);

        sendOrderConfirmationEmail(order.getId(), generateEBookInvoice(order.getId()));
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
            order.getOrderBooks().forEach(orderBook -> bookRepository.updateQuantity(orderBook.getBook().getId(), orderBook.getBook().getQuantity() + 1));
            orderRepository.deleteById(orderId);
        }
    }

    @Override
    public void confirmPreOrder(Integer orderId) throws MessagingException, FileNotFoundException, JRException {
        changeOrderStatusToORDERED(orderId);

        sendOrderConfirmationEmail(orderId, generateInvoice(orderId));
    }

    @Override
    public void applyCouponToOrder(Integer orderId, Integer couponId){
        Order order = orderRepository.findOrderById(orderId);
        Coupon coupon = couponRepository.findCouponById(couponId);
        order.setCoupon(coupon);
        coupon.setUsed(true);

        if(coupon.getBookQuantity() == null) {
            order.setTotalPrice(order.getTotalPrice()
                    .subtract(order.getTotalPrice().multiply(BigDecimal.valueOf(coupon.getDiscount() / 100.0))));
        } else {
            Integer bookQuantity = coupon.getBookQuantity();
            order.getOrderBooks().stream().limit(bookQuantity).forEach(orderBook -> {
                BigDecimal discount = orderBook.getBook().getPrice().multiply(BigDecimal.valueOf(coupon.getDiscount() / 100.0));
                order.setTotalPrice(order.getTotalPrice().subtract(discount));
            });
        }
        orderRepository.save(order);
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

    private void sendOrderStatusUpdatedEmail(Integer orderId) throws MessagingException {
        Order order = orderRepository.findOrderById(orderId);

        Map<String, Object> mailContext = new HashMap<>();
        mailContext.put("orderId", orderId);
        mailContext.put("status", order.getStatus().getName());
        mailContext.put("username", order.getBuyer().getUsername());

        mailSender.sendEmail(order.getBuyer().getEmail(), "GoldenBook order",
                                MailSenderImpl.MailType.ORDER_STATUS_UPDATE, mailContext);
    }

    private void sendOrderConfirmationEmail(Integer orderId, String attachmentFile) throws MessagingException {
        Order order = orderRepository.findOrderById(orderId);

        Map<String, Object> mailContext = new HashMap<>();
        mailContext.put("orderId", orderId);
        mailContext.put("attachmentFile", attachmentFile);
        mailContext.put("username", order.getBuyer().getUsername());

        mailSender.sendEmail(order.getBuyer().getEmail(), "GoldenBook order confirmed",
                                MailSenderImpl.MailType.ORDER_CONFIRMED, mailContext);
    }

    private String generateInvoice(Integer orderId) throws FileNotFoundException, JRException {
        List<Book> bookList = new ArrayList<>();
        Map<String, Object> parameter = new HashMap<>();

        Order order = orderRepository.findOrderById(orderId);
        order.getOrderBooks().forEach(orderBook -> bookList.add(orderBook.getBook()));

        parameter.put("dataSource", new JRBeanCollectionDataSource(bookList));
        parameter.put("orderId", orderId);
        parameter.put("orderTotalPrice", order.getTotalPrice());

        String outFileName = invoiceRepository + "GoldenBookInvoice" + orderId + ".pdf";
        pdfGenerator.createPdfFile("src/main/resources/templates/invoice-template.jrxml", outFileName, parameter);

        return outFileName;
    }

    private String generateEBookInvoice(Integer orderId) throws FileNotFoundException, JRException {
        Map<String, Object> parameter = new HashMap<>();
        Order order = orderRepository.findOrderById(orderId);
        Book book = order.getOrderBooks().stream().findFirst().get().getBook();

        parameter.put("orderId", orderId);
        parameter.put("title", book.getTitle());
        parameter.put("author", book.getAuthor().getName() + " " + book.getAuthor().getSurname());
        parameter.put("price", book.getEbook().getPrice());

        String outFileName = invoiceRepository + "GoldenBookInvoice" + orderId + ".pdf";
        pdfGenerator.createPdfFile("src/main/resources/templates/e-book-invoice-template.jrxml", outFileName, parameter);

        return outFileName;
    }
}
