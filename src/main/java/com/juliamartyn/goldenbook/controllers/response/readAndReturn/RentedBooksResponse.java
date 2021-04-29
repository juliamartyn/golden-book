package com.juliamartyn.goldenbook.controllers.response.readAndReturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentedBooksResponse {
    private Integer historyId;
    private LocalDate expectedReturnDate;
    private ReadAndReturnBooksResponse book;
}
