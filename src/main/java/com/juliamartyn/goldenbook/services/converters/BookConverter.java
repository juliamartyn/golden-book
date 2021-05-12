package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.request.BookRequest;
import com.juliamartyn.goldenbook.controllers.response.BookPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.BookResponse;
import com.juliamartyn.goldenbook.entities.Author;
import com.juliamartyn.goldenbook.entities.Book;
import com.juliamartyn.goldenbook.repository.AuthorRepository;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class BookConverter {

    private final ModelMapper modelMapper;
    private final BookCategoryRepository bookCategoryRepository;
    private final AuthorRepository authorRepository;

    public Book of(BookRequest bookRequest) {
        Book map = modelMapper.map(bookRequest, Book.class);
        map.setCategory(bookCategoryRepository.findByName(bookRequest.getCategory()));

        Object[] authorArray =  Arrays.stream(bookRequest.getAuthor().trim().split(" ")).toArray();
        String authorName = authorArray[0].toString();
        String authorSurname = authorArray[1].toString();
        Author author = authorRepository.findAuthorByNameAndSurname(authorName, authorSurname);
        if(author != null){
            map.setAuthor(author);
        } else{
            map.setAuthor(authorRepository.save(Author.builder().name(authorName).surname(authorSurname).build()));
        }

        return map;
    }

    public BookResponse of(Book book){
        BookResponse map =  modelMapper.map(book, BookResponse.class);
        map.setCategory(book.getCategory().getName());

        Author author = book.getAuthor();
        map.setAuthor(author.getName() + " " + author.getSurname());

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
