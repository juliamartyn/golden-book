package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.LoginRequest;
import com.juliamartyn.goldenbook.controllers.request.RegisterUserRequest;
import com.juliamartyn.goldenbook.controllers.response.UserResponse;
import com.juliamartyn.goldenbook.security.jwt.JwtAuthProvider;
import com.juliamartyn.goldenbook.security.jwt.JwtResponse;
import com.juliamartyn.goldenbook.security.services.UserPrinciple;
import com.juliamartyn.goldenbook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtAuthProvider jwtAuthProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtAuthProvider jwtAuthProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtAuthProvider = jwtAuthProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtAuthProvider.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                                                    userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        return new ResponseEntity<>(userService.register(registerUserRequest), HttpStatus.CREATED);
    }
}