package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.CreateUserRequest;
import com.juliamartyn.goldenbook.controllers.request.DeliveryAddressRequest;
import com.juliamartyn.goldenbook.controllers.request.DisableUserRequest;
import com.juliamartyn.goldenbook.controllers.request.UsernameUpdateRequest;
import com.juliamartyn.goldenbook.controllers.response.UserPageableResponse;
import com.juliamartyn.goldenbook.controllers.response.UserResponse;
import com.juliamartyn.goldenbook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userService.create(createUserRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SELLER')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SELLER')")
    @GetMapping("/page/{pageNo}")
    public ResponseEntity<UserPageableResponse> pageableUsers(@PathVariable int pageNo) {
        return new ResponseEntity<>(userService.findPageableUsers(pageNo, 10), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableUserRequest request) {
        userService.updateDisabled(id, request.isDisabled());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/username")
    public ResponseEntity<Void> updateUsername(@PathVariable Integer id, @RequestBody UsernameUpdateRequest request) {
        userService.updateUsername(id, request.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping("/{id}/delivery-address")
    public ResponseEntity<Void> updateDeliveryAddress(@PathVariable Integer id, @RequestBody DeliveryAddressRequest request) {
        userService.updateDeliveryAddress(id, request.getDeliveryAddress());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
