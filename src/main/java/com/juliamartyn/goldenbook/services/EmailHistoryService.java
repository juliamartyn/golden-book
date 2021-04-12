package com.juliamartyn.goldenbook.services;

import com.juliamartyn.goldenbook.controllers.response.EmailHistoryPageableResponse;

import javax.mail.MessagingException;

public interface EmailHistoryService {
    EmailHistoryPageableResponse findPageableEmails(int pageNo, int pageSize);
    void resendEmail(Integer emailHistoryItemId) throws MessagingException;
}
