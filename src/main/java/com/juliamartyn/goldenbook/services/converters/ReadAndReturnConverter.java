package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.request.readAndReturn.ReadAndReturnCreateRequest;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.ReadAndReturnBooksResponse;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.RentedBooksResponse;
import com.juliamartyn.goldenbook.entities.readAndReturn.ReadAndReturnHistory;
import com.juliamartyn.goldenbook.entities.readAndReturn.ReadAndReturnTariff;
import com.juliamartyn.goldenbook.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ReadAndReturnConverter {

    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final BookConverter bookConverter;

    public ReadAndReturnTariff toReadAndReturnTariff(ReadAndReturnCreateRequest request) {
        ReadAndReturnTariff map =  modelMapper.map(request, ReadAndReturnTariff.class);
        map.setBook(bookRepository.findBookById(request.getBookId()));
        return map;
    }

    public ReadAndReturnBooksResponse toReadAndReturnBookResponse(ReadAndReturnTariff readAndReturnTariff) {
        ReadAndReturnBooksResponse map =  modelMapper.map(readAndReturnTariff, ReadAndReturnBooksResponse.class);
        map.setBook(bookConverter.of(readAndReturnTariff.getBook()));
        return map;
    }

    public RentedBooksResponse toRentedBookResponse(ReadAndReturnHistory readAndReturnHistory) {
        RentedBooksResponse map =  modelMapper.map(readAndReturnHistory, RentedBooksResponse.class);
        map.setHistoryId(readAndReturnHistory.getId());
        map.setBook(toReadAndReturnBookResponse(readAndReturnHistory.getTariff()));
        return map;
    }
}
