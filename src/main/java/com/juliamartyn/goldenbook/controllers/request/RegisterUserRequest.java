package com.juliamartyn.goldenbook.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    private String username;
    private String password;
    private String email;
    private String phone;
}