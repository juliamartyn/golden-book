package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.services.MailSender;
import org.springframework.beans.factory.annotation.Value;
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
    public void sendEmail(String emailTo, String subject, Map<String, Object> mailContext) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

        Context context = new Context();
        context.setVariables(mailContext);

        String process = templateEngine.process(mailContext.get("template").toString(), context);

        helper.setSubject(subject);
        helper.setText(process, true);
        helper.setTo(emailTo);
        helper.setFrom(username);

        mailSender.send(mailMessage);
    }

}
