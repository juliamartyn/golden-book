package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.entities.EOrder;
import com.juliamartyn.goldenbook.entities.Order;
import com.juliamartyn.goldenbook.repository.EOrderRepository;
import com.juliamartyn.goldenbook.repository.OrderRepository;
import com.juliamartyn.goldenbook.services.AmazonS3ClientService;
import com.juliamartyn.goldenbook.services.EOrderService;
import com.juliamartyn.goldenbook.services.MailSender;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EOrderServiceImpl implements EOrderService {

    @Value("${S3_FOLDER_NAME}")
    @Getter
    private String folderName;

    @Value("${S3_BUCKET_NAME}")
    @Getter
    private String bucketName;

    @Value("${DOWNLOAD_LINK}")
    private String downloadLink;

    private final EOrderRepository eOrderRepository;
    private final OrderRepository orderRepository;
    private final MailSender mailSender;
    private final AmazonS3ClientService amazonS3ClientService;

    public EOrderServiceImpl(EOrderRepository eOrderRepository, OrderRepository orderRepository, MailSender mailSender, AmazonS3ClientService amazonS3ClientService) {
        this.eOrderRepository = eOrderRepository;
        this.orderRepository = orderRepository;
        this.mailSender = mailSender;
        this.amazonS3ClientService = amazonS3ClientService;
    }

    @Override
    public void sendEOrderEmail(Integer orderId) throws MessagingException {
        Order order = orderRepository.findOrderById(orderId);
        EOrder eOrder = EOrder.builder()
                .order(order)
                .expirationDate(LocalDate.now().plusDays(3))
                .code(UUID.randomUUID().toString())
                .build();

        eOrderRepository.save(eOrder);

        Map<String, Object> mailContext = new HashMap<>();
        mailContext.put("username", order.getBuyer().getUsername());
        mailContext.put("link", downloadLink + eOrder.getCode());

        mailSender.sendEmail(order.getBuyer().getEmail(), "GoldenBook E-Order",
                MailSenderImpl.MailType.E_ORDER, mailContext);
    }

    @Override
    public byte[] downloadByCode(String code) {
        String file_ref = eOrderRepository.findFileReferenceByEOrderCode(code);

        return amazonS3ClientService.downloadFile(file_ref, bucketName, folderName);
    }
}
