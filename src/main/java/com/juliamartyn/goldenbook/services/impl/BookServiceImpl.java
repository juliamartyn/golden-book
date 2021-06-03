package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.request.DiscountRequest;
import com.juliamartyn.goldenbook.controllers.request.EBookRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Discount;
import com.juliamartyn.goldenbook.entities.EBook;
import com.juliamartyn.goldenbook.entities.Favorite;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.DiscountRepository;
import com.juliamartyn.goldenbook.repository.EBookRepository;
import com.juliamartyn.goldenbook.repository.FavoriteRepository;
import com.juliamartyn.goldenbook.services.AmazonS3ClientService;
import com.juliamartyn.goldenbook.services.BookService;
import com.juliamartyn.goldenbook.services.MailSender;
import com.juliamartyn.goldenbook.services.converters.BookConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${S3_FOLDER_NAME}")
    private String folderName;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    @Value("${BOOK_PAGE_BASE_LINK}")
    private String BOOK_PAGE_BASE_LINK;

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    private final DiscountRepository discountRepository;
    private final AmazonS3ClientService amazonS3ClientService;
    private final EBookRepository eBookRepository;
    private final MailSender mailSender;
    private final FavoriteRepository favoriteRepository;
    private final BookCategoryRepository bookCategoryRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           BookConverter bookConverter,
                           DiscountRepository discountRepository,
                           AmazonS3ClientService amazonS3ClientService,
                           EBookRepository eBookRepository,
                           MailSender mailSender,
                           FavoriteRepository favoriteRepository,
                           BookCategoryRepository bookCategoryRepository) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.discountRepository = discountRepository;
        this.amazonS3ClientService = amazonS3ClientService;
        this.eBookRepository = eBookRepository;
        this.mailSender = mailSender;
        this.favoriteRepository = favoriteRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @Override
    public BookResponse create(BookRequest bookRequest, MultipartFile image, MultipartFile fileEBook) throws MessagingException {
        Book book = bookConverter.of(bookRequest);
        try {
            book.setImage(image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(fileEBook != null){
            EBook eBook = EBook.builder()
                    .fileReference(amazonS3ClientService.uploadFileToS3(fileEBook, bucketName, folderName))
                    .price(bookRequest.getEBookPrice())
                    .build();
            book.setEbook(eBookRepository.save(eBook));
        }

        Book createdBook = bookRepository.save(book);
        sendNewFromFavoriteEmailNotification(book);

        return bookConverter.of(createdBook);
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

    @Override
    public void addEBook(Integer bookId, MultipartFile fileEBook, EBookRequest eBookRequest){
        EBook eBook = EBook.builder()
                .fileReference(amazonS3ClientService.uploadFileToS3(fileEBook, bucketName, folderName))
                .price(eBookRequest.getPrice())
                .build();

        eBook = eBookRepository.save(eBook);
        bookRepository.updateEBook(bookId, eBook.getId());
    }

    private void sendNewFromFavoriteEmailNotification(Book book) throws MessagingException {
        Map<String, Object> mailContext = new HashMap<>();
        User recipient;

        for (Favorite item : favoriteRepository.findAll()){
            recipient = item.getCustomer();

            mailContext.put("username", recipient.getUsername());
            mailContext.put("book", book.getTitle() + " " + book.getAuthor().getName() + book.getAuthor().getSurname());
            mailContext.put("link", BOOK_PAGE_BASE_LINK + book.getId() + "/details");

            mailSender.sendEmail(recipient.getEmail(), "GoldenBook new book",
                        MailSenderImpl.MailType.NEW_AT_FAVORITE, mailContext);
        }
    }
}
