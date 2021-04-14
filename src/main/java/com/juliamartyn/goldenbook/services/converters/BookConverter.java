package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class BookConverter {

    private final ModelMapper modelMapper;
    private final BookCategoryRepository bookCategoryRepository;

    public Book of(BookRequest bookRequest) {
        Book map = modelMapper.map(bookRequest, Book.class);
        map.setCategory(bookCategoryRepository.findByName(bookRequest.getCategory()));
        return map;
    }

    public BookResponse of(Book book){
        BookResponse map =  modelMapper.map(book, BookResponse.class);
        map.setCategory(book.getCategory().getName());
        return map;
    }

    public BookPageableResponse toBookPageableResponse(Page<Book> books) {
        return BookPageableResponse.builder()
                .bookList(books.toList().stream().map(this::of).collect(Collectors.toList()))
                .totalItems(books.getTotalElements())
                .totalPages(books.getTotalPages())
                .currentPage(books.getNumber())
                .build();
    }
}
