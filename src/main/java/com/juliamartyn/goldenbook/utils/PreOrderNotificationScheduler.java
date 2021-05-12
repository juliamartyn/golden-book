package com.juliamartyn.goldenbook.utils;

import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Order;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.repository.OrderRepository;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.impl.MailSenderImpl;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PreOrderNotificationScheduler {

    private final OrderRepository orderRepository;
    private final MailSender mailSender;

    @Scheduled(cron = "0 0 6 * * ?")
    public void sendNotificationByMail() throws MessagingException {
        List<Order> PreOrdersWithStartSellingTodayBooks = orderRepository.findAllPreOrdersWithStartSellingTodayBooks();
        if (!PreOrdersWithStartSellingTodayBooks.isEmpty()) {
            for (Order order : PreOrdersWithStartSellingTodayBooks) {
                User buyer = order.getBuyer();
                Book book = order.getOrderBooks().stream().findFirst().get().getBook();
                String bookTitle = book.getTitle() + " " + book.getAuthor();

                mailSender.sendEmail(buyer.getEmail(), "Pre-Ordered book is available now",
                        MailSenderImpl.MailType.BOOK_AVAILABLE, Map.of("username", buyer.getUsername(), "book", bookTitle));
            }
        }
    }
}
