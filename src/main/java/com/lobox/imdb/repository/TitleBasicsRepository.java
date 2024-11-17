package com.lobox.imdb.repository;

import com.lobox.imdb.domain.TitleBasics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleBasicsRepository extends JpaRepository<TitleBasics, String> {
    @Query(value = """
            WITH RankedTitles AS (
                SELECT
                    tb.tconst,
                    tb.primary_title,
                    tb.start_year,
                    tr.average_rating,
                    tr.num_votes,
                    ROW_NUMBER() OVER (
                        PARTITION BY tb.start_year
                        ORDER BY tr.num_votes DESC, tr.average_rating DESC
                    ) AS row_num
                FROM title_basics tb
                JOIN title_ratings tr ON tb.tconst = tr.tconst
                JOIN title_genres tg ON tb.tconst = tg.tconst
                WHERE tg.genre = :genre
            )
            SELECT tconst, primary_title, start_year, average_rating, num_votes
            FROM RankedTitles
            WHERE row_num = 1;
            """, nativeQuery = true)
    List<Object[]> findBestTitlesByGenre(@Param("genre") String genre);
}
