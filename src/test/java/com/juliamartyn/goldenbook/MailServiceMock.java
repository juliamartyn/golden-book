package com.juliamartyn.goldenbook;

import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.impl.MailSenderImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Profile("test")
public class MailServiceMock implements MailSender {

    @Override
    public void sendEmail(String emailTo, String subject, MailSenderImpl.MailType mailType, Map<String, Object> mailContext) {
    }
}