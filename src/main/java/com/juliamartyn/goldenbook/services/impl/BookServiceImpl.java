package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.request.DiscountRequest;
import com.juliamartyn.goldenbook.controllers.request.EBookRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.entities.Discount;
import com.juliamartyn.goldenbook.entities.EBook;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.DiscountRepository;
import com.juliamartyn.goldenbook.repository.EBookRepository;
import com.juliamartyn.goldenbook.services.BookService;
import com.juliamartyn.goldenbook.services.converters.BookConverter;
import com.juliamartyn.goldenbook.services.s3.AmazonS3ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    private final DiscountRepository discountRepository;
    private final AmazonS3ClientService amazonS3ClientService;
    private final EBookRepository eBookRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           BookConverter bookConverter,
                           DiscountRepository discountRepository,
                           AmazonS3ClientService amazonS3ClientService,
                           EBookRepository eBookRepository) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.discountRepository = discountRepository;
        this.amazonS3ClientService = amazonS3ClientService;
        this.eBookRepository = eBookRepository;
    }

    @Override
    public BookResponse create(BookRequest bookRequest, MultipartFile image, MultipartFile fileEBook){
        Book book = bookConverter.of(bookRequest);
        try {
            book.setImage(image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!fileEBook.isEmpty()){
            EBook eBook = EBook.builder()
                    .fileReference(uploadEBookToS3(fileEBook))
                    .price(bookRequest.getEBookPrice())
                    .build();
            book.setEbook(eBookRepository.save(eBook));
        }

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

    @Override
    public void addEBook(Integer bookId, MultipartFile fileEBook, EBookRequest eBookRequest){
        EBook eBook = EBook.builder()
                .fileReference(uploadEBookToS3(fileEBook))
                .price(eBookRequest.getPrice())
                .build();

        eBook = eBookRepository.save(eBook);
        bookRepository.updateEBook(bookId, eBook.getId());
    }

    private String uploadEBookToS3(MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        String extension = split[split.length - 1];
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;
        try {
            amazonS3ClientService.upload(amazonS3ClientService.getBucketName(),
                    amazonS3ClientService.getFolderName() + "/" + fileReference, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileReference;
    }
}
