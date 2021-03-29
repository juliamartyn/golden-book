package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.services.BookService;
import com.juliamartyn.goldenbook.services.converters.BookConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;

    public BookServiceImpl(BookRepository bookRepository, BookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }

    @Override
    public BookResponse create(BookRequest bookRequest, MultipartFile file){
        Book book = bookConverter.of(bookRequest);
        try {
            book.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
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
}
