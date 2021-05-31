package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.readAndReturn.ReadAndReturnCreateRequest;
import com.juliamartyn.goldenbook.controllers.request.readAndReturn.ReadAndReturnTariffPricePerDayRequest;
import com.juliamartyn.goldenbook.controllers.request.readAndReturn.RentBookRequest;
import com.juliamartyn.goldenbook.controllers.request.readAndReturn.EmailRemindingRequest;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.ReadAndReturnBooksResponse;
import com.juliamartyn.goldenbook.controllers.response.readAndReturn.RentedBooksResponse;
import com.juliamartyn.goldenbook.security.services.UserPrinciple;
import com.juliamartyn.goldenbook.services.ReadAndReturnService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/read-return")
public class ReadAndReturnController {

    private final ReadAndReturnService readAndReturnService;

    public ReadAndReturnController(ReadAndReturnService readAndReturnService) {
        this.readAndReturnService = readAndReturnService;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ReadAndReturnCreateRequest request) {
        readAndReturnService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PatchMapping("/{id}/price-per-day")
    public ResponseEntity<Void> updatePricePerDay(@PathVariable Integer id, @RequestBody ReadAndReturnTariffPricePerDayRequest request) {
        readAndReturnService.updatePricePerDay(id, request.getPricePerDay());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER') OR hasAuthority('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<ReadAndReturnBooksResponse>> findAllReadAndReturnBooks(){
        return new ResponseEntity<>(readAndReturnService.findAllReadAndReturnBooks(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @GetMapping("/rented")
    public ResponseEntity<List<RentedBooksResponse>> findAllRentedBooks(){
        return new ResponseEntity<>(readAndReturnService.findAllRentedBooks(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/rent")
    public ResponseEntity<RentedBooksResponse> rentBook(@RequestBody RentBookRequest request, Authentication authentication) throws FileNotFoundException, JRException, MessagingException {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();
        return new ResponseEntity<>(readAndReturnService.rentBook(request, currentUser.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping("/{id}/email-reminding")
    public ResponseEntity<Void> updateEmailReminding(@PathVariable Integer id, @RequestBody EmailRemindingRequest request) {
        readAndReturnService.updateEmailReminding(id, request.isEmailReminding());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
