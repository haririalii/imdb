package com.lobox.imdb.repository;

import com.lobox.imdb.domain.NameBasics;
import com.lobox.imdb.domain.TitleBasics;
import com.lobox.imdb.domain.TitleCrew;
import com.lobox.imdb.domain.TitlePrincipals;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TitleQueryBuilder {

    private final EntityManager entityManager;

    public TitleQueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<TitleBasics> findTitlesWithFilters(
            String genre,
            Boolean sameDirectorTitle,
            Boolean alive,
            List<String> actorIds,
            int start,
            int length
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TitleBasics> query = cb.createQuery(TitleBasics.class);
        Root<TitleBasics> titleRoot = query.from(TitleBasics.class);
        Join<TitleBasics, TitleCrew> crewJoin = titleRoot.join("titleCrew", JoinType.LEFT);
        Join<TitleCrew, NameBasics> directorJoin = crewJoin.join("directors", JoinType.LEFT);

        query.select(titleRoot);

        List<Predicate> predicates = new ArrayList<>();

        if (sameDirectorTitle != null && sameDirectorTitle) {
            Path<String> directorNconst = directorJoin.get("nconst");
            Join<TitleCrew, NameBasics> writersJoin = crewJoin.join("writers", JoinType.LEFT);
            Predicate directorInWriters = cb.equal(directorNconst, writersJoin.get("nconst"));
            predicates.add(directorInWriters);
        }

        if (alive != null && alive) {
            predicates.add(cb.isNull(directorJoin.get("deathYear")));
        }

        if (genre != null && !genre.isBlank()) {
            predicates.add(cb.isMember(genre, titleRoot.get("genres")));
        }

        if (actorIds != null && !actorIds.isEmpty()) {
            for (String actorId : actorIds) {
                Subquery<String> actorSubquery = query.subquery(String.class);
                Root<TitlePrincipals> actorRoot = actorSubquery.from(TitlePrincipals.class);
                actorSubquery.select(actorRoot.get("titleBasics").get("tconst"))
                        .where(cb.equal(actorRoot.get("nameBasics").get("nconst"), actorId));
                predicates.add(titleRoot.get("tconst").in(actorSubquery));
            }
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<TitleBasics> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(length);

        return typedQuery.getResultList();
    }
}
