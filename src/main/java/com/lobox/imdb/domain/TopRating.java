package com.lobox.imdb.domain;

import lombok.Data;

@Data
public class TopRating {

    private String tconst;
    private String primaryTitle;
    private String year;
    private String averageRating;
    private String numVotes;
}
