package com.juliamartyn.goldenbook.services;

import javax.mail.MessagingException;

public interface EOrderService {
    void sendEOrderEmail(Integer orderId) throws MessagingException;
    byte[] downloadByCode(String code);
}
