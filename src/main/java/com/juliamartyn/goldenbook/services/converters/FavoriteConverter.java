package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.request.FavoriteRequest;
import com.juliamartyn.goldenbook.controllers.response.FavoriteResponse;
import com.juliamartyn.goldenbook.entities.Favorite;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FavoriteConverter {

    private final ModelMapper modelMapper;
    private final BookCategoryRepository categoryRepository;

    public Favorite of(FavoriteRequest favoriteRequest) {
        Favorite map = modelMapper.map(favoriteRequest, Favorite.class);
        if (favoriteRequest.getType().equals("CATEGORY")){
            map.setEntityId(categoryRepository.findByName(favoriteRequest.getItemName()).getId());
            map.setType(Favorite.FavoriteType.CATEGORY);
        }

        return map;
    }

    public FavoriteResponse of(Favorite favorite){
        FavoriteResponse map =  modelMapper.map(favorite, FavoriteResponse.class);
        map.setItemName(categoryRepository.findById(favorite.getEntityId()).get().getName());
        return map;
    }
}
