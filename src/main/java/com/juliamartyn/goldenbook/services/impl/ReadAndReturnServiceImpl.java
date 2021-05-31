package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.readAndReturn.ReadAndReturnCreateRequest;
import com.juliamartyn.goldenbook.controllers.request.readAndReturn.RentBookRequest;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.ReadAndReturnBooksResponse;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.RentedBooksResponse;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.entities.readAndReturn.ReadAndReturnHistory;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.repository.readAndReturn.ReadAndReturnHistoryRepository;
import com.juliamartyn.goldenbook.repository.readAndReturn.ReadAndReturnTariffRepository;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.ReadAndReturnService;
import com.juliamartyn.goldenbook.services.converters.BookConverter;
import com.juliamartyn.goldenbook.services.converters.ReadAndReturnConverter;
import com.juliamartyn.goldenbook.utils.PdfGenerator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReadAndReturnServiceImpl implements ReadAndReturnService {

    @Value("${INVOICE_REPOSITORY}")
    private String invoiceRepository;

    private final ReadAndReturnTariffRepository tariffRepository;
    private final ReadAndReturnHistoryRepository historyRepository;
    private final ReadAndReturnConverter readAndReturnConverter;
    private final BookConverter bookConverter;
    private final UserRepository userRepository;
    private final PdfGenerator pdfGenerator;
    private final MailSender mailSender;

    public ReadAndReturnServiceImpl(ReadAndReturnTariffRepository tariffRepository,
                                    ReadAndReturnHistoryRepository historyRepository, ReadAndReturnConverter readAndReturnConverter, BookConverter bookConverter, UserRepository userRepository, PdfGenerator pdfGenerator, MailSender mailSender) {
        this.tariffRepository = tariffRepository;
        this.historyRepository = historyRepository;
        this.readAndReturnConverter = readAndReturnConverter;
        this.bookConverter = bookConverter;
        this.userRepository = userRepository;
        this.pdfGenerator = pdfGenerator;
        this.mailSender = mailSender;
    }

    @Override
    public void create(ReadAndReturnCreateRequest request) {
        tariffRepository.save(readAndReturnConverter.toReadAndReturnTariff(request));
    }

    @Override
    public void updatePricePerDay(Integer id, BigDecimal pricePerDay) {
        if (tariffRepository.updatePricePerDay(id, pricePerDay) == 0){
            throw new NotFoundException("Read&Return tariff with id " + id + " not found");
        }
    }

    @Override
    public List<ReadAndReturnBooksResponse> findAllReadAndReturnBooks(){
        return tariffRepository.findAllReadAndReturnBooks().stream()
                .map(readAndReturnConverter::toReadAndReturnBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentedBooksResponse> findAllRentedBooks(){
        return historyRepository.findAllRentedBooks().stream()
                .map(readAndReturnConverter::toRentedBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateEmailReminding(Integer id, boolean emailRemaining) {
        if(historyRepository.updateEmailReminding(id, emailRemaining) == 0){
            throw new NotFoundException("Read and Return history item not found");
        }
    }

    @Override
    public RentedBooksResponse rentBook(RentBookRequest request, Long currentUserId) throws FileNotFoundException, JRException, MessagingException {
        ReadAndReturnHistory readAndReturnHistory = ReadAndReturnHistory.builder()
                .tariff(tariffRepository.findById(request.getTariffId()).get())
                .rentDaysNumber(request.getRentDaysNumber())
                .customer(userRepository.findUserById(currentUserId))
                .expectedReturnDate(LocalDate.now().plusDays(request.getRentDaysNumber()))
                .build();

        RentedBooksResponse rentedBooksResponse = readAndReturnConverter.toRentedBookResponse(historyRepository.save(readAndReturnHistory));

        Integer historyId = rentedBooksResponse.getHistoryId();
        sendRentConfirmationEmail(historyId, generateInvoice(historyId));

        return rentedBooksResponse;
    }

    private String generateInvoice(Integer historyId) throws FileNotFoundException, JRException {
        List<BookResponse> bookList = new ArrayList<>();
        Map<String, Object> parameter = new HashMap<>();

        ReadAndReturnHistory historyItem = historyRepository.findById(historyId).get();
        BigDecimal totalPrice = historyItem.getTariff().getBook().getPrice();

        bookList.add(bookConverter.of(historyItem.getTariff().getBook()));
        parameter.put("dataSource", new JRBeanCollectionDataSource(bookList));
        parameter.put("orderId", historyId);
        parameter.put("orderTotalPrice", totalPrice);

        String outFileName = invoiceRepository + "GoldenBookInvoiceRent" + historyId + ".pdf";
        pdfGenerator.createPdfFile("src/main/resources/templates/invoice-template.jrxml", outFileName, parameter);

        return outFileName;
    }

    private void sendRentConfirmationEmail(Integer historyId, String attachmentFile) throws MessagingException {
        Map<String, Object> mailContext = new HashMap<>();
        User buyer = historyRepository.findById(historyId).get().getCustomer();
        mailContext.put("orderId", historyId);
        mailContext.put("attachmentFile", attachmentFile);
        mailContext.put("username", buyer.getUsername());

        mailSender.sendEmail(buyer.getEmail(), "GoldenBook rent confirmed",
                MailSenderImpl.MailType.ORDER_CONFIRMED, mailContext);
    }
}
