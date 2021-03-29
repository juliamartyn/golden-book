package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.CreateUserRequest;
import com.juliamartyn.goldenbook.controllers.request.RegisterUserRequest;
import com.juliamartyn.goldenbook.controllers.response.UserResponse;
import com.juliamartyn.goldenbook.entities.Role;
import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.exception.NotFoundException;
import com.juliamartyn.goldenbook.repository.RoleRepository;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.services.UserService;
import com.juliamartyn.goldenbook.services.converters.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;

    @Override
    public UserResponse create(CreateUserRequest createUserRequest){
        User user = User.builder()
                .username(createUserRequest.getUsername())
                .password(encoder.encode(createUserRequest.getPassword()))
                .email(createUserRequest.getEmail())
                .phone(createUserRequest.getPhone())
                .build();

        Role role = roleRepository.findByName(createUserRequest.getRole());
        user.setRole(role);
        return userConverter.of(userRepository.save(user));
    }

    @Override
    public UserResponse register(RegisterUserRequest registerUserRequest) {
        User user = User.builder()
                .username(registerUserRequest.getUsername())
                .password(encoder.encode(registerUserRequest.getPassword()))
                .email(registerUserRequest.getEmail())
                .phone(registerUserRequest.getPhone())
                .build();

        user.setRole(roleRepository.findByName("ROLE_CUSTOMER"));
        return userConverter.of(userRepository.save(user));
    }

    @Override
    public void updateDisabled(Integer id, Boolean disabled){
        if(userRepository.updateDisabled(id, disabled) == 0){
            throw new NotFoundException("User with id " + id + "not found");
        }
    }

    @Override
    public void updateUsername(Integer id, String username) {
        if(userRepository.updateUsername(id, username) == 0){
            throw new NotFoundException("User with id " + id + "not found");
        }
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findUserById(Long id) {
        return userConverter.of(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found")));
    }
}
