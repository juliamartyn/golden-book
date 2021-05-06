package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.request.DiscountRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Discount;
import com.juliamartyn.goldenbook.entities.Favorite;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.DiscountRepository;
import com.juliamartyn.goldenbook.repository.FavoriteRepository;
import com.juliamartyn.goldenbook.services.BookService;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.converters.BookConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    private final DiscountRepository discountRepository;
    private final MailSender mailSender;
    private final FavoriteRepository favoriteRepository;
    private final BookCategoryRepository bookCategoryRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           BookConverter bookConverter,
                           DiscountRepository discountRepository, MailSender mailSender, FavoriteRepository favoriteRepository, BookCategoryRepository bookCategoryRepository) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.discountRepository = discountRepository;
        this.mailSender = mailSender;
        this.favoriteRepository = favoriteRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @Override
    public BookResponse create(BookRequest bookRequest, MultipartFile file) throws MessagingException {
        Book book = bookConverter.of(bookRequest);
        try {
            book.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendNewFromFavoriteEmailNotification(book);
        return bookConverter.of(bookRepository.save(book));
    }

    @Override
    public void updateQuantity(Integer id, Integer quantity) {
        if(bookRepository.updateQuantity(id, quantity) == 0){
            throw new NotFoundException("Book with id " + id + "not found");
        }
    }

    @Override
    public void updatePrice(Integer id, BigDecimal price) {
        if(bookRepository.updatePrice(id, price) == 0){
            throw new NotFoundException("Book with id " + id + " not found");
        }
    }

    @Override
    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(bookConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public BookPageableResponse findPageableBooks(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<Book> pagedResult = bookRepository.findAll(paging);

        return bookConverter.toBookPageableResponse(pagedResult);
    }

    @Override
    public BookResponse findBookById(Integer id) {
        return bookConverter.of(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found")));
    }

    @Override
    public List<BookResponse> findTopSellingBooksByCategory(Integer category) {
        return bookRepository.findTopSellingBooksByCategoryId(category).stream()
                .map(bookConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void addDiscount(DiscountRequest discountRequest) {
        Discount discount = Discount.builder()
                .discountPercent(discountRequest.getDiscount())
                .dueDate(discountRequest.getDueDate())
                .build();
        discountRepository.save(discount);

        discountRequest.getBooksId().forEach(bookId -> {
                    Book book = bookRepository.findBookById(bookId);
                    book.setDiscount(discount);
                    book.setPriceWithDiscount(book.getPrice().subtract(book.getPrice()
                            .multiply(BigDecimal.valueOf(book.getDiscount().getDiscountPercent() / 100.0))));
                    bookRepository.save(book);
                });
    }

    private void sendNewFromFavoriteEmailNotification(Book book) throws MessagingException {
        Map<String, Object> mailContext = new HashMap<>();
        boolean sendEmail = false;
        User recipient;

        for (Favorite item : favoriteRepository.findAll()){
            recipient = item.getCustomer();
            if(item.getType().toString().equals("CATEGORY")){
                sendEmail = bookCategoryRepository.findById(item.getEntityId()).get().equals(book.getCategory());
            }

            if (sendEmail) {
                mailContext.put("username", recipient.getUsername());
                mailContext.put("book", book.getTitle() + " " + book.getAuthor());

                mailSender.sendEmail(recipient.getEmail(), "GoldenBook new book",
                        MailSenderImpl.MailType.NEW_AT_FAVORITE, mailContext);
            }
        }
    }
}
