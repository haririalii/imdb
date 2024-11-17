package com.lobox.imdb.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class TitleCrewDTO {

    private List<NameBasicsDTO> directors;
    private List<NameBasicsDTO> writers;
}
