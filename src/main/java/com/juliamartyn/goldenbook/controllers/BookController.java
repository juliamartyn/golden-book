package com.juliamartyn.goldenbook.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliamartyn.goldenbook.controllers.request.BookPriceRequest;
import com.juliamartyn.goldenbook.controllers.request.BookQuantityRequest;
import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.request.DiscountRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;


@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public BookController(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestPart(value = "book") String book,
                                               @RequestPart(value = "file",  required = false) MultipartFile file) throws JsonProcessingException, MessagingException {
        BookRequest request = objectMapper.readValue(book, BookRequest.class);
        return new ResponseEntity<>(bookService.create(request, file), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/page/{pageNo}")
    public ResponseEntity<BookPageableResponse> booksPageable(@PathVariable int pageNo) {
        return new ResponseEntity<>(bookService.findPageableBooks(pageNo, 10), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')  OR hasAuthority('ROLE_SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable Integer id) {
        return new ResponseEntity<>(bookService.findBookById(id), HttpStatus.OK);
    }

    @GetMapping("/top/{categoryId}")
    public ResponseEntity<List<BookResponse>> topSellingBooksByCategory(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(bookService.findTopSellingBooksByCategory(categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable Integer id, @RequestBody BookPriceRequest request) {
        bookService.updatePrice(id, request.getPrice());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Void> updateQuantity(@PathVariable Integer id, @RequestBody BookQuantityRequest request) {
        bookService.updateQuantity(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_ADMIN')")
    @PostMapping("/apply-discount")
    public ResponseEntity<Void> applyDiscount(@RequestBody DiscountRequest discountRequest){
        bookService.addDiscount(discountRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
