package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.services.EOrderService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("api/e-orders")
public class EOrderController {
    private final EOrderService eOrderService;

    public EOrderController(EOrderService eOrderService) {
        this.eOrderService = eOrderService;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping("orders/{order_id}")
    public ResponseEntity<Void> sendEOrder(@PathVariable Integer order_id) throws MessagingException {
        eOrderService.sendEOrderEmail(order_id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/download/{code}")
    public ResponseEntity<byte[]> downloadByCode(@PathVariable String code){
        byte[] file = eOrderService.downloadByCode(code);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename("GoldenBook", StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(headers)
                .body(file);
    }
}
