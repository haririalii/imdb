package com.lobox.imdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "title_principals")
public class TitlePrincipals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Integer ordering;
    private String category;
    private String job;

    @ManyToOne
    @JoinColumn(name = "nconst", referencedColumnName = "nconst", nullable = false)
    private NameBasics nameBasics;

    @ManyToOne
    @JoinColumn(name = "tconst", insertable = false, updatable = false)
    private TitleBasics titleBasics;

    @ElementCollection
    @CollectionTable(name = "title_principals_characters", joinColumns = @JoinColumn(name = "principal_id"))
    @Column(name = "character")
    private List<String> characters;
}
