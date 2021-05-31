package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import com.juliamartyn.goldenbook.controllers.response.UserResponse;
import com.juliamartyn.goldenbook.entities.Author;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.BookCategory;
import com.juliamartyn.goldenbook.entities.Coupon;
import com.juliamartyn.goldenbook.entities.Order;
import com.juliamartyn.goldenbook.entities.OrderBook;
import com.juliamartyn.goldenbook.entities.OrderStatus;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.CouponRepository;
import com.juliamartyn.goldenbook.repository.OrderBookRepository;
import com.juliamartyn.goldenbook.repository.OrderRepository;
import com.juliamartyn.goldenbook.repository.OrderStatusRepository;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.converters.BookConverter;
import com.juliamartyn.goldenbook.services.converters.OrderConverter;
import com.juliamartyn.goldenbook.utils.PdfGenerator;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private OrderConverter mockOrderConverter;
    @Mock
    private BookRepository mockBookRepository;
    @Mock
    private OrderStatusRepository mockOrderStatusRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private MailSender mockMailSender;
    @Mock
    private PdfGenerator mockPdfGenerator;
    @Mock
    private CouponRepository mockCouponRepository;
    @Mock
    private OrderBookRepository mockOrderBookRepository;
    @Mock
    private BookConverter mockBookConverter;

    private OrderServiceImpl orderServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        orderServiceImplUnderTest = new OrderServiceImpl(mockOrderRepository, mockOrderConverter, mockBookRepository,
                                                            mockOrderStatusRepository, mockUserRepository,
                                                            mockCouponRepository, mockMailSender,
                                                            mockPdfGenerator , mockOrderBookRepository, mockBookConverter);
    }

    @Test
    void testAddBookWhenSavedOrderAlreadyExists() {
        when(mockBookRepository.findBookById(anyInt())).thenReturn(testBook());
        when(mockOrderRepository.findOrderWithStatusSavedByBuyerId(anyLong())).thenReturn(testOrder());
        when(mockOrderConverter.of(any(Order.class))).thenReturn(testOrderResponse());
        when(mockOrderRepository.save(any(Order.class))).thenReturn(testOrder());

        final OrderResponse result = orderServiceImplUnderTest.addBook(1, 0L);

        assertEquals(1, result.getBuyer().getId());
        verify(mockOrderRepository, times(1)).save(any(Order.class));
    }


    @Test
    void testAddBookWhenSavedOrderNotExists() {
        when(mockBookRepository.findBookById(anyInt())).thenReturn(testBook());
        when(mockOrderRepository.findOrderWithStatusSavedByBuyerId(anyLong())).thenReturn(null);
        when(mockOrderConverter.of(any(Order.class))).thenReturn(testOrderResponse());
        when(mockOrderRepository.save(any(Order.class))).thenReturn(testOrder());

        final OrderResponse result = orderServiceImplUnderTest.addBook(1, 1L);

        assertEquals(1, result.getBuyer().getId());
        verify(mockOrderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testConfirmOrder() throws MessagingException, FileNotFoundException, JRException {
        final OrderStatus orderStatus = OrderStatus.builder().id(1).name("NAME").build();

        when(mockOrderStatusRepository.findByName("ORDERED")).thenReturn(orderStatus);
        when(mockOrderRepository.updateStatus(1, 1)).thenReturn(1);
        when(mockOrderRepository.findOrderById(1)).thenReturn(testOrder());
        when(mockBookRepository.updateQuantity(1, 0)).thenReturn(1);

        orderServiceImplUnderTest.confirmOrder(1);

        verify(mockBookRepository, times(1)).updateQuantity(1,0);
        verify(mockOrderRepository, times(3)).findOrderById(1);
        verify(mockPdfGenerator, times(1)).createPdfFile(anyString(),anyString(), anyMap());
    }

    @Test
    void testApplyCouponToOrder() {
        Order order = testOrder();
        BigDecimal orderPriceWithoutCoupon = order.getTotalPrice();

        when(mockOrderRepository.findOrderById(1)).thenReturn(order);
        when(mockCouponRepository.findCouponById(1)).thenReturn(testCoupon());

        orderServiceImplUnderTest.applyCouponToOrder(1, 1);

        verify(mockOrderRepository, times(1)).findOrderById(1);
        verify(mockCouponRepository, times(1)).findCouponById(1);
        verify(mockOrderRepository, times(1)).save(order);
        assertNotEquals(orderPriceWithoutCoupon, order.getTotalPrice());
    }


    @Test
    void testPreOrder() {
        final OrderStatus orderStatus = OrderStatus.builder().id(1).name("PREORDERED").build();
        when(mockOrderStatusRepository.findByName(anyString())).thenReturn(orderStatus);

        when(mockUserRepository.findUserById(anyLong())).thenReturn(new User());
        when(mockBookRepository.findBookById(anyInt())).thenReturn(testBook());
        when(mockBookRepository.updateQuantity(1, 0)).thenReturn(1);
        when(mockOrderRepository.save(any(Order.class))).thenReturn(testOrder());
        when(mockOrderBookRepository.save(any(OrderBook.class))).thenReturn(testOrderBook(testOrder()));

        orderServiceImplUnderTest.preOrder(1, 1L);

        verify(mockOrderRepository, times(1)).save(any(Order.class));
        verify(mockOrderBookRepository, times(1)).save(any(OrderBook.class));
    }

    private Book testBook(){
        return Book.builder()
                .id(1)
                .title("title")
                .author(new Author())
                .price(BigDecimal.valueOf(100))
                .quantity(1)
                .image("image".getBytes())
                .category(new BookCategory())
                .priceWithDiscount(BigDecimal.valueOf(100))
                .build();
    }

    private Order testOrder(){
         Order order = Order.builder()
                .id(1)
                .totalPrice(BigDecimal.valueOf(100))
                .status(new OrderStatus())
                .buyer(new User())
                .build();

        order.setOrderBooks(Set.of(testOrderBook(order)));
         return order;
    }

    private OrderBook testOrderBook(Order order){
        return OrderBook.builder()
                .bookType( OrderBook.BookType.PAPER)
                .book(testBook())
                .order(order).build();
    }

    private OrderResponse testOrderResponse(){
        return OrderResponse.builder()
                .id(1)
                .status("status")
                .buyer(new UserResponse(1L, "username", "email", "phone",
                        "address", false, "ROLE_CUSTOMER"))
                .totalPrice(BigDecimal.valueOf(100))
                .books(List.of(new BookResponse(1, "title", "author", "description",
                        "category", BigDecimal.valueOf(100), 1, LocalDate.now(), "image".getBytes(),
                        BigDecimal.valueOf(100), null, null )))
                .build();
    }

    private Coupon testCoupon(){
        return Coupon.builder()
                .id(1)
                .discount(10)
                .bookQuantity(2)
                .build();
    }

}
