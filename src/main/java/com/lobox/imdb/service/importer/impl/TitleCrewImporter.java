package com.lobox.imdb.service.importer.impl;

import com.lobox.imdb.service.importer.AbstractImporter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TitleCrewImporter extends AbstractImporter {

    @Override
    protected void saveBatch(List<CSVRecord> records) {
        String mainTable = "title_crew";
        List<String> mainColumns = List.of("tconst");

        String directorsTable = "title_directors";
        List<String> directorsColumns = List.of("tconst", "director");

        String writersTable = "title_writers";
        List<String> writersColumns = List.of("tconst", "writer");

        List<List<Object>> mainData = new ArrayList<>();
        List<List<Object>> directorsData = new ArrayList<>();
        List<List<Object>> writersData = new ArrayList<>();

        for (CSVRecord record : records) {
            mainData.add(List.of(record.get("tconst")));

            String[] directors = record.get("directors").split(",");
            for (String director : directors) {
                if (!director.isBlank()) {
                    directorsData.add(List.of(record.get("tconst"), director));
                }
            }

            String[] writers = record.get("writers").split(",");
            for (String writer : writers) {
                if (!writer.isBlank()) {
                    writersData.add(List.of(record.get("tconst"), writer));
                }
            }
        }

        if (!mainData.isEmpty()) {
            String mainSQL = generateBatchInsertSQL(mainTable, mainData, mainColumns);
            jdbcTemplate.update(mainSQL);
        }

        if (!directorsData.isEmpty()) {
            String directorsSQL = generateBatchInsertSQL(directorsTable, directorsData, directorsColumns);
            jdbcTemplate.update(directorsSQL);
        }

        if (!writersData.isEmpty()) {
            String writersSQL = generateBatchInsertSQL(writersTable, writersData, writersColumns);
            jdbcTemplate.update(writersSQL);
        }
    }

    @Override
    public String getType() {
        return "title_crew";
    }
}
