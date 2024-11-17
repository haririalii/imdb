package com.lobox.imdb.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class TitlePrincipalsDTO {

    private Integer ordering;
    private String category;
    private String job;
    private NameBasicsDTO nameBasics;
    private List<String> characters;
}
