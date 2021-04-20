package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.response.UserPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.UserResponse;
import com.juliamartyn.goldenbook.entities.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserConverter {

    private final ModelMapper modelMapper;

    public UserResponse of(User user){
        UserResponse map =  modelMapper.map(user, UserResponse.class);
        map.setRole(user.getRole().getName());
        return map;
    }

    public UserPageableResponse toUserPageableResponse(Page<User> users) {
        return UserPageableResponse.builder()
                .users(users.stream().map(this::of).collect(Collectors.toList()))
                .totalItems(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .currentPage(users.getNumber())
                .build();
    }
}
