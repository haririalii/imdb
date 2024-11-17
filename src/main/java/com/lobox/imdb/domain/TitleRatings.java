package com.lobox.imdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "title_ratings")
public class TitleRatings {

    @Id
    private String tconst;
    private double averageRating;
    private int numVotes;

    @OneToOne
    @JoinColumn(name = "tconst", referencedColumnName = "tconst")
    private TitleBasics titleBasics;
}
