package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.OrderStatusRequest;
import com.juliamartyn.goldenbook.controllers.response.OrderResponse;
import com.juliamartyn.goldenbook.security.services.UserPrinciple;
import com.juliamartyn.goldenbook.services.OrderService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SELLER')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> orders() {
        return new ResponseEntity<>(orderService.findAllConfirmedOrders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/current-user")
    public ResponseEntity<List<OrderResponse>> buyerOrders(Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        return new ResponseEntity<>(orderService.findOrdersByBuyerId(currentUser.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/cart")
    public ResponseEntity<OrderResponse> cart(Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        return new ResponseEntity<>(orderService.findOrderWithStatusSavedByBuyerId(currentUser.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/pre-orders")
    public ResponseEntity<List<OrderResponse>> preOrders(Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        return new ResponseEntity<>(orderService.findPreOrdersByBuyerId(currentUser.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PutMapping("/add-book/{bookId}")
    public ResponseEntity<OrderResponse> addBook(@PathVariable Integer bookId, Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        return new ResponseEntity<>(orderService.addBook(bookId, currentUser.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer id, @RequestBody OrderStatusRequest request) throws MessagingException {
        orderService.updateStatus(id, request.getStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping("/{orderId}/cart/delete-book/{bookId}")
    public ResponseEntity<Void> deleteBookFromCart(@PathVariable Integer orderId, @PathVariable Integer bookId) {
        orderService.deleteBookFromCart(orderId, bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable Integer id) throws MessagingException, FileNotFoundException, JRException {
        orderService.confirmOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/preorder-book/{bookId}")
    public ResponseEntity<Void> preOrderBook(@PathVariable Integer bookId, Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        orderService.preOrder(bookId, currentUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping("/pre-orders/{id}/confirm")
    public ResponseEntity<Void> confirmPreOrder(@PathVariable Integer id) throws MessagingException, FileNotFoundException, JRException {
        orderService.confirmPreOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPreOrder(@PathVariable Integer id) {
        orderService.cancelPreOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
