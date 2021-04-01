package com.juliamartyn.goldenbook.services;


import com.juliamartyn.goldenbook.services.impl.MailSenderImpl;

import javax.mail.MessagingException;
import java.util.Map;

public interface MailSender {
    void sendEmail(String emailTo, String subject, MailSenderImpl.MailType mailType, Map<String, Object> mailContext) throws MessagingException;
}
