package com.lobox.imdb.service.mapper;

import com.lobox.imdb.controller.dto.TitleDTO;
import com.lobox.imdb.domain.TitleBasics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TitleMapper {

    TitleDTO titleBasicsToTitleDTO(TitleBasics titleBasics);
}
