package com.lobox.imdb.service.importer.impl;

import com.lobox.imdb.service.importer.AbstractImporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class TitlePrincipalsImporter extends AbstractImporter {

    AtomicLong autoGeneratedId = null;

    @Override
    protected void saveBatch(List<CSVRecord> records) {
        String mainTable = "title_principals";
        List<String> mainColumns = List.of("id", "tconst", "ordering", "nconst", "category", "job");

        String charactersTable = "title_principals_characters";
        List<String> charactersColumns = List.of("principal_id", "character");

        List<List<Object>> mainData = new ArrayList<>();
        List<List<Object>> charactersData = new ArrayList<>();

        if (autoGeneratedId == null) {
            autoGeneratedId = new AtomicLong(getStartingIdForMainTable());
        }

        for (CSVRecord record : records) {
            mainData.add(List.of(
                    autoGeneratedId.longValue(),
                    record.get("tconst"),
                    parseInteger(record.get("ordering")),
                    record.get("nconst"),
                    record.get("category"),
                    toNullString(record.get("job").equals("\\N") ? null : record.get("job"))
            ));

            String charactersField = record.get("characters");
            if (!charactersField.equals("\\N")) {
                String[] characters = parseJsonArray(charactersField);
                for (String character : characters) {
                    charactersData.add(List.of(autoGeneratedId.longValue(), character));
                }
            }

            autoGeneratedId.incrementAndGet();
        }

        if (!mainData.isEmpty()) {
            String mainSQL = generateBatchInsertSQL(mainTable, mainData, mainColumns);
            jdbcTemplate.update(mainSQL);
        }

        if (!charactersData.isEmpty()) {
            String charactersSQL = generateBatchInsertSQL(charactersTable, charactersData, charactersColumns);
            jdbcTemplate.update(charactersSQL);
        }
    }


    @Override
    public String getType() {
        return "title_principals";
    }

    /**
     * Helper method to parse a JSON array string into an array of strings.
     */
    private String[] parseJsonArray(String json) {
        return json.replaceAll("\\[|\\]", "").replaceAll("\"", "").split(",");
    }

    /**
     * Fetch the starting ID for the current batch.
     */
    private long getStartingIdForMainTable() {
        Long maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), '0') FROM title_principals", Long.class);
        return maxId != null ? maxId + 1 : 1;
    }
}