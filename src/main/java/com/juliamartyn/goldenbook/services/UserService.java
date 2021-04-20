package com.juliamartyn.goldenbook.services;

import com.juliamartyn.goldenbook.controllers.request.CreateUserRequest;
import com.juliamartyn.goldenbook.controllers.request.RegisterUserRequest;
import com.juliamartyn.goldenbook.controllers.response.UserPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(CreateUserRequest createUserRequest);
    UserResponse register(RegisterUserRequest registerUserRequest);
    void updateDisabled(Integer id, Boolean disabled);
    void updateUsername(Integer id, String username);
    void updateDeliveryAddress(Integer id, String address);
    List<UserResponse> findAll();
    UserResponse findUserById(Long id);
    UserPageableResponse findPageableUsers(int pageNo, int pageSize);
}
