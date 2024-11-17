package com.lobox.imdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "title_basics")
public class TitleBasics {

    @Id
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private Integer startYear;
    private Integer endYear;
    private Integer runtimeMinutes;

    @ElementCollection
    @CollectionTable(name = "title_genres", joinColumns = @JoinColumn(name = "tconst"))
    @Column(name = "genre")
    private List<String> genres;

    @OneToOne(mappedBy = "titleBasics", cascade = CascadeType.ALL, orphanRemoval = true)
    private TitleRatings titleRatings;

    @OneToOne(mappedBy = "titleBasics", cascade = CascadeType.ALL, orphanRemoval = true)
    private TitleCrew titleCrew;

    @OneToMany(mappedBy = "titleBasics", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TitlePrincipals> titlePrincipals;
}
