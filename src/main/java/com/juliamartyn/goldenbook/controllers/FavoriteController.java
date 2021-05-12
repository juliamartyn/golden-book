package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.FavoriteRequest;
import com.juliamartyn.goldenbook.controllers.response.FavoriteResponse;
import com.juliamartyn.goldenbook.security.services.UserPrinciple;
import com.juliamartyn.goldenbook.services.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/add")
    public ResponseEntity<FavoriteResponse> addToFavorite(@RequestBody FavoriteRequest request, Authentication authentication) {
        UserPrinciple currentUser = (UserPrinciple) authentication.getPrincipal();

        return new ResponseEntity<>(favoriteService.addToFavorite(request, currentUser.getId()), HttpStatus.OK);
    }
}
