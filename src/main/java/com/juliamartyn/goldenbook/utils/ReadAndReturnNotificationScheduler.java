package com.juliamartyn.goldenbook.utils;

import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.entities.readAndReturn.ReadAndReturnHistory;
import com.juliamartyn.goldenbook.repository.readAndReturn.ReadAndReturnHistoryRepository;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.impl.MailSenderImpl;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReadAndReturnNotificationScheduler {

    private final ReadAndReturnHistoryRepository readAndReturnHistoryRepository;
    private final MailSender mailSender;

    @Scheduled(cron = "0 0 6 * * ?")
    public void sendRemindNotificationByMail() throws MessagingException {
        List<ReadAndReturnHistory> lastRentDay = readAndReturnHistoryRepository.findAllWithLastRentDay();
        if (!lastRentDay.isEmpty()) {
            for (ReadAndReturnHistory item : lastRentDay) {
                User buyer = item.getCustomer();
                String book = item.getTariff().getBook().getTitle() + " " + item.getTariff().getBook().getAuthor();

                mailSender.sendEmail(buyer.getEmail(), "Book's last renting day",
                        MailSenderImpl.MailType.LAST_RENT_DAY, Map.of("username", buyer.getUsername(), "book", book));
            }
        }
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void sendWarningNotificationByMail() throws MessagingException {
        List<ReadAndReturnHistory> history = readAndReturnHistoryRepository.findAll();
        if (!history.isEmpty()) {
            for (ReadAndReturnHistory item : history) {
                Integer days = Period.between(LocalDate.now() , item.getExpectedReturnDate()).getDays();

                if(item.isEmailReminding() && days >= 1 && days <= 3){
                    User buyer = item.getCustomer();
                    String book = item.getTariff().getBook().getTitle() + " " + item.getTariff().getBook().getAuthor();

                    mailSender.sendEmail(buyer.getEmail(), "Last book renting day",
                            MailSenderImpl.MailType.RENT_DATE_REMINDER, Map.of("username", buyer.getUsername(),
                                    "book", book, "daysNumber", days));
                }
            }
        }
    }

}
