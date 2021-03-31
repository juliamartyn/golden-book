package com.juliamartyn.goldenbook.services;


import javax.mail.MessagingException;
import java.util.Map;

public interface MailSender {
    void sendEmail(String emailTo, String subject,  Map<String, Object> mailContext) throws MessagingException;
}
