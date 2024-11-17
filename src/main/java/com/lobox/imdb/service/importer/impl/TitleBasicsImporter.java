package com.lobox.imdb.service.importer.impl;

import com.lobox.imdb.service.importer.AbstractImporter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TitleBasicsImporter extends AbstractImporter {

    @Override
    protected void saveBatch(List<CSVRecord> records) {
        String mainTable = "title_basics";
        List<String> mainColumns = List.of("tconst", "title_type", "primary_title", "original_title", "is_adult", "start_year", "end_year", "runtime_minutes");

        String genreTable = "title_genres";
        List<String> genreColumns = List.of("tconst", "genre");

        List<List<Object>> mainData = new ArrayList<>();
        List<List<Object>> genreData = new ArrayList<>();

        for (CSVRecord record : records) {
            mainData.add(List.of(
                    record.get("tconst"),
                    record.get("titleType"),
                    record.get("primaryTitle"),
                    record.get("originalTitle"),
                    Boolean.parseBoolean(record.get("isAdult")),
                    toNullString(parseInteger(record.get("startYear"))),
                    toNullString(parseInteger(record.get("endYear"))),
                    toNullString(parseInteger(record.get("runtimeMinutes")))
            ));

            String[] genres = record.get("genres").split(",");
            for (String genre : genres) {
                if (!genre.isBlank()) {
                    genreData.add(List.of(record.get("tconst"), genre));
                }
            }
        }

        if (!mainData.isEmpty()) {
            String mainSQL = generateBatchInsertSQL(mainTable, mainData, mainColumns);
            jdbcTemplate.update(mainSQL);
        }

        if (!genreData.isEmpty()) {
            String genreSQL = generateBatchInsertSQL(genreTable, genreData, genreColumns);
            jdbcTemplate.update(genreSQL);
        }
    }

    @Override
    public String getType() {
        return "title_basics";
    }
}
