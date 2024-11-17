package com.lobox.imdb.service.impl;

import com.lobox.imdb.controller.dto.TitleDTO;
import com.lobox.imdb.domain.TopRating;
import com.lobox.imdb.repository.TitleBasicsRepository;
import com.lobox.imdb.repository.TitleQueryBuilder;
import com.lobox.imdb.service.TitleService;
import com.lobox.imdb.service.mapper.TitleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultTitleService implements TitleService {

    private final TitleBasicsRepository titleBasicsRepository;
    private final TitleQueryBuilder titleQueryBuilder;
    private final TitleMapper titleMapper;

    @Override
    public List<TitleDTO> getTitles(Boolean sameDirectorTitle,
                                    Boolean alive,
                                    List<String> actorIds,
                                    String genre,
                                    int start,
                                    int length) {
        return titleQueryBuilder.findTitlesWithFilters(
                        genre,
                        sameDirectorTitle,
                        alive,
                        actorIds,
                        start,
                        length
                ).stream()
                .map(titleMapper::titleBasicsToTitleDTO)
                .toList();
    }

    @Override
    public List<TopRating> getBestTitlesByGenre(String genre) {
        List<Object[]> rawResults = titleBasicsRepository.findBestTitlesByGenre(genre);

        return rawResults.stream().map(row -> {
            TopRating topRating = new TopRating();
            topRating.setTconst((String) row[0]);
            topRating.setPrimaryTitle((String) row[1]);
            topRating.setYear(String.valueOf(row[2]));
            topRating.setAverageRating(String.valueOf(row[3]));
            topRating.setNumVotes(String.valueOf(row[4]));
            return topRating;
        }).toList();
    }
}
