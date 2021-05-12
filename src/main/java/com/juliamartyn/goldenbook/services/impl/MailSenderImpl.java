package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.services.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class MailSenderImpl  implements MailSender {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public MailSenderImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Value("{spring.mail.username}")
    private String username;

    @Override
    public void sendEmail(String emailTo, String subject, MailType mailType, Map<String, Object> mailContext) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

        Context context = new Context();
        context.setVariables(mailContext);

        String process = templateEngine.process(mailType.getTemplate(), context);

        helper.setSubject(subject);
        helper.setText(process, true);
        helper.setTo(emailTo);
        helper.setFrom(username);

        if(mailContext.get("attachmentFile") != null){
            FileSystemResource file = new FileSystemResource((String) mailContext.get("attachmentFile"));
            helper.addAttachment(file.getFilename(), file);
        }

        mailSender.send(mailMessage);
    }

    public enum MailType{
        ORDER_STATUS_UPDATE("order-status-temp"),
        ORDER_CONFIRMED("order-confirm-temp"),
        BOOK_AVAILABLE("book-available-temp"),
        LAST_RENT_DAY("last-rent-day-temp"),
        RENT_DATE_REMINDER("read-and-return-reminder-temp"),
        NEW_AT_FAVORITE("favorite-new-book-temp"),
        E_ORDER("e-order-temp");

        private String template;

        MailType(String template) {
            this.template = template;
        }

        public String getTemplate() {
            return template;
        }
    }

}
