package com.lobox.imdb.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class TitleDTO {

    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private Integer startYear;
    private Integer endYear;
    private Integer runtimeMinutes;
    private TitleCrewDTO titleCrew;
    private TitleRatingsDTO titleRatings;
    private List<TitlePrincipalsDTO> titlePrincipals;

}
