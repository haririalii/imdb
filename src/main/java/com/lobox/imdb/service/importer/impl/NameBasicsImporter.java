package com.lobox.imdb.service.importer.impl;

import com.lobox.imdb.service.importer.AbstractImporter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NameBasicsImporter extends AbstractImporter {

    @Override
    protected void saveBatch(List<CSVRecord> records) {
        String mainTable = "name_basics";
        List<String> mainColumns = List.of("nconst", "primary_name", "birth_year", "death_year");

        String professionTable = "primary_professions";
        List<String> professionColumns = List.of("nconst", "profession");

        String knownForTable = "known_for_titles";
        List<String> knownForColumns = List.of("nconst", "title");

        List<List<Object>> mainData = new ArrayList<>();
        List<List<Object>> professionData = new ArrayList<>();
        List<List<Object>> knownForData = new ArrayList<>();

        for (CSVRecord record : records) {
            mainData.add(List.of(
                    record.get("qnconst"),
                    record.get("primaryName"),
                    toNullString(parseInteger(record.get("birthYear"))),
                    toNullString(parseInteger(record.get("deathYear")))
            ));

            String[] professions = record.get("primaryProfession").split(",");
            for (String profession : professions) {
                professionData.add(List.of(record.get("qnconst"), profession));
            }

            String[] titles = record.get("knownForTitles").split(",");
            for (String title : titles) {
                knownForData.add(List.of(record.get("qnconst"), title));
            }
        }

        if (!mainData.isEmpty()) {
            String mainSQL = generateBatchInsertSQL(mainTable, mainData, mainColumns);
            jdbcTemplate.update(mainSQL);
        }

        if (!professionData.isEmpty()) {
            String professionSQL = generateBatchInsertSQL(professionTable, professionData, professionColumns);
            jdbcTemplate.update(professionSQL);
        }

        if (!knownForData.isEmpty()) {
            String knownForSQL = generateBatchInsertSQL(knownForTable, knownForData, knownForColumns);
            jdbcTemplate.update(knownForSQL);
        }
    }

    @Override
    public String getType() {
        return "name_basics";
    }
}
