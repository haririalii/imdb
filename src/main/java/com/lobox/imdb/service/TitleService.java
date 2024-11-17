package com.lobox.imdb.service;

import com.lobox.imdb.controller.dto.TitleDTO;
import com.lobox.imdb.domain.TopRating;

import java.util.List;

public interface TitleService {

    List<TitleDTO> getTitles(Boolean sameDirectorTitle, Boolean alive, List<String> actorIds, String genre, int start, int length);
    List<TopRating> getBestTitlesByGenre(String genre);
}
