package com.juliamartyn.goldenbook.utils;

import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.repository.DiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DiscountCleanScheduler {

    private final DiscountRepository discountRepository;
    private final BookRepository bookRepository;

    @Scheduled(cron = "0 59 23 * * ?")
    public void deleteDiscountByDueDate(){
        discountRepository.findAll().forEach(discount -> {
            if(discount.getDueDate().isEqual(LocalDate.now())){
                bookRepository.findBooksByDiscountId(discount.getId()).forEach(book -> {
                    book.setPriceWithDiscount(book.getPrice());
                    bookRepository.save(book);
                });
                discountRepository.delete(discount);
            }
        });
    }
}
