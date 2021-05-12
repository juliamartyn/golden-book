package com.juliamartyn.goldenbook.services.converters;

import com.juliamartyn.goldenbook.controllers.request.FavoriteRequest;
import com.juliamartyn.goldenbook.controllers.response.FavoriteResponse;
import com.juliamartyn.goldenbook.entities.Author;
import com.juliamartyn.goldenbook.entities.Favorite;
import com.juliamartyn.goldenbook.repository.AuthorRepository;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@AllArgsConstructor
@Component
public class FavoriteConverter {

    private final ModelMapper modelMapper;
    private final BookCategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public Favorite of(FavoriteRequest favoriteRequest) {
        Favorite map = modelMapper.map(favoriteRequest, Favorite.class);

        if (favoriteRequest.getType().equals("CATEGORY")){
            map.setEntityId(categoryRepository.findByName(favoriteRequest.getItemName()).getId());
            map.setType(Favorite.FavoriteType.CATEGORY);
        } else if(favoriteRequest.getType().equals("AUTHOR")){
            Object[] authorArray =  Arrays.stream(favoriteRequest.getItemName().split(" ")).toArray();
            String authorName = authorArray[0].toString();
            String authorSurname = authorArray[1].toString();

            map.setEntityId(authorRepository.findAuthorByNameAndSurname(authorName, authorSurname).getId());
            map.setType(Favorite.FavoriteType.AUTHOR);
        }
        return map;
    }

    public FavoriteResponse of(Favorite favorite){
        FavoriteResponse map =  modelMapper.map(favorite, FavoriteResponse.class);

        if(favorite.getType().name().equals("CATEGORY")){
            map.setItemName(categoryRepository.findById(favorite.getEntityId()).get().getName());
        } else if(favorite.getType().name().equals("AUTHOR")){
            Author author = authorRepository.findById(favorite.getEntityId()).get();
            map.setItemName(author.getName() + " " + author.getSurname());
        }
        return map;
    }
}
