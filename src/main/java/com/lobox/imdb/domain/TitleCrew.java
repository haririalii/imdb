package com.lobox.imdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "title_crew")
public class TitleCrew {

    @Id
    private String tconst;

    @ManyToMany
    @JoinTable(
            name = "title_directors",
            joinColumns = @JoinColumn(name = "tconst"),
            inverseJoinColumns = @JoinColumn(name = "director", referencedColumnName = "nconst")
    )
    private List<NameBasics> directors;

    @ManyToMany
    @JoinTable(
            name = "title_writers",
            joinColumns = @JoinColumn(name = "tconst"),
            inverseJoinColumns = @JoinColumn(name = "writer", referencedColumnName = "nconst")
    )
    private List<NameBasics> writers;

    @OneToOne
    @JoinColumn(name = "tconst", referencedColumnName = "tconst")
    private TitleBasics titleBasics;

}
