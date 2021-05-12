package com.juliamartyn.goldenbook.services.impl;

import com.juliamartyn.goldenbook.controllers.request.FavoriteRequest;
import com.juliamartyn.goldenbook.controllers.response.FavoriteResponse;
import com.juliamartyn.goldenbook.entities.Favorite;
import com.juliamartyn.goldenbook.repository.FavoriteRepository;
import com.juliamartyn.goldenbook.repository.UserRepository;
import com.juliamartyn.goldenbook.services.FavoriteService;
import com.juliamartyn.goldenbook.services.converters.FavoriteConverter;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteConverter favoriteConverter;
    private final UserRepository userRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteConverter favoriteConverter, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteConverter = favoriteConverter;
        this.userRepository = userRepository;
    }

    @Override
    public FavoriteResponse addToFavorite(FavoriteRequest favoriteRequest, Long currentUserId){
        Favorite favorite = favoriteConverter.of(favoriteRequest);
        favorite.setCustomer(userRepository.findUserById(currentUserId));

        return favoriteConverter.of(favoriteRepository.save(favorite));
    }
}
