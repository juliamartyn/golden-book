package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPageableResponse {
    private List<BookResponse> bookList;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}
