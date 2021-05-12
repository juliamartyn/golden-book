package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.response.EmailHistoryPageableResponse;
import com.juliamartyn.goldenbook.entities.EmailHistory;
import com.juliamartyn.goldenbook.entities.Order;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.repository.EmailHistoryRepository;
import com.juliamartyn.goldenbook.services.EmailHistoryService;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.converters.EmailHistoryConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailHistoryServiceImpl implements EmailHistoryService {

    @Value("${INVOICE_REPOSITORY}")
    private String invoiceRepository;

    private final EmailHistoryRepository emailHistoryRepository;
    private final EmailHistoryConverter emailHistoryConverter;
    private final MailSender mailSender;

    public EmailHistoryServiceImpl(EmailHistoryRepository emailHistoryRepository, EmailHistoryConverter emailHistoryConverter, MailSender mailSender) {
        this.emailHistoryRepository = emailHistoryRepository;
        this.emailHistoryConverter = emailHistoryConverter;
        this.mailSender = mailSender;
    }

    @Override
    public EmailHistoryPageableResponse findPageableEmails(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<EmailHistory> pagedResult = emailHistoryRepository.findAll(paging);

        return emailHistoryConverter.toEmailHistoryPageableResponse(pagedResult);
    }

    @Override
    public void resendEmail(Integer emailHistoryItemId) throws MessagingException {
        EmailHistory emailHistoryItem = emailHistoryRepository.findEmailHistoryById(emailHistoryItemId);
        String emailType = emailHistoryItem.getEmailType().toString();
        Order order = emailHistoryItem.getOrder();
        User buyer = emailHistoryItem.getOrder().getBuyer();

        Map<String, Object> mailContext = new HashMap<>();
        mailContext.put("orderId", order.getId());
        mailContext.put("username", buyer.getUsername());

        switch (emailType){
            case "ORDER_CONFIRMED":
                mailContext.put("attachmentFile", invoiceRepository + "GoldenBookInvoice" + order.getId() + ".pdf");
                break;
            case "BOOK_AVAILABLE":
                mailContext.put("book", order.getBooks().get(0).getTitle() + " " + order.getBooks().get(0).getAuthor());
                break;
            case "ORDER_STATUS_UPDATE":
                mailContext.put("status", order.getStatus().getName());
                break;
        }

        mailSender.sendEmail(buyer.getEmail(), "GoldenBook store", MailSenderImpl.MailType.valueOf(emailType), mailContext);
    }
}
