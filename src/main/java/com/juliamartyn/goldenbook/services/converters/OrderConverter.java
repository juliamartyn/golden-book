package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import com.juliamartyn.goldenbook.entities.Order;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class OrderConverter {

    private final ModelMapper modelMapper;
    private final BookConverter bookConverter;

    public OrderResponse of(Order order){
        OrderResponse map =  modelMapper.map(order, OrderResponse.class);

        map.setBooks(order.getOrderBooks().stream()
                .map(orderBook -> bookConverter.of(orderBook.getBook()))
                .collect(Collectors.toList()));

        map.setStatus(order.getStatus().getName());
        return map;
    }
}
