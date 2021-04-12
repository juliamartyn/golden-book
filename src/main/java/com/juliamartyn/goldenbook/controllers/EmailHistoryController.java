package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.response.EmailHistoryPageableResponse;
import com.juliamartyn.goldenbook.services.EmailHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("api/emails")
public class EmailHistoryController {

    private final EmailHistoryService emailHistoryService;

    public EmailHistoryController(EmailHistoryService emailHistoryService) {
        this.emailHistoryService = emailHistoryService;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @GetMapping("/{pageNo}")
    public ResponseEntity<EmailHistoryPageableResponse> emailHistory(@PathVariable int pageNo) {
        return new ResponseEntity<>(emailHistoryService.findPageableEmails(pageNo, 10), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping("/resend/{id}")
    public ResponseEntity<Void> resendEmail(@PathVariable Integer id) throws MessagingException {
        emailHistoryService.resendEmail(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
