package com.juliamartyn.goldenbook.services;

import com.juliamartyn.goldenbook.controllers.request.FavoriteRequest;
import com.juliamartyn.goldenbook.controllers.response.FavoriteResponse;

public interface FavoriteService {
    FavoriteResponse addToFavorite(FavoriteRequest favoriteRequest, Long currentUserId);
}
