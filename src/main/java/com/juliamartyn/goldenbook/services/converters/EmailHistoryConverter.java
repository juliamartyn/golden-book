package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.response.EmailHistoryPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.EmailHistoryResponse;
import com.juliamartyn.goldenbook.entities.EmailHistory;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class EmailHistoryConverter {

    private final ModelMapper modelMapper;

    public EmailHistoryPageableResponse toEmailHistoryPageableResponse(Page<EmailHistory> emails) {
        return EmailHistoryPageableResponse.builder()
                .emailHistory(emails.toList().stream().map(this::toEmailHistoryResponse).collect(Collectors.toList()))
                .totalItems(emails.getTotalElements())
                .totalPages(emails.getTotalPages())
                .currentPage(emails.getNumber())
                .build();
    }

    public EmailHistoryResponse toEmailHistoryResponse(EmailHistory emailHistory){
        EmailHistoryResponse map = modelMapper.map(emailHistory, EmailHistoryResponse.class);

        map.setBuyer(emailHistory.getOrder().getBuyer().getEmail());

        if(emailHistory.getEmailType().equals("BOOK_AVAILABLE")){
            Boolean availableToResend = emailHistory.getOrder().getBooks().stream().allMatch(book -> book.getStartSaleDate().isAfter(LocalDate.now()));
            map.setAvailableToResend(availableToResend);
        }

        return map;
    }

}
