package com.lobox.imdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "name_basics")
public class NameBasics {

    @Id
    private String nconst;
    private String primaryName;
    private Integer birthYear;
    private Integer deathYear;

    @ElementCollection
    @CollectionTable(name = "known_for_titles", joinColumns = @JoinColumn(name = "nconst"))
    @Column(name = "title")
    private List<String> knownForTitles;

    @ElementCollection
    @CollectionTable(name = "primary_professions", joinColumns = @JoinColumn(name = "nconst"))
    @Column(name = "profession")
    private List<String> primaryProfessions;

}
