package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.response.UserResponse;
import com.juliamartyn.goldenbook.entities.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserConverter {

    private final ModelMapper modelMapper;

    public UserResponse of(User user){
        UserResponse map =  modelMapper.map(user, UserResponse.class);
        map.setRole(user.getRole().getName());
        return map;
    }
}
