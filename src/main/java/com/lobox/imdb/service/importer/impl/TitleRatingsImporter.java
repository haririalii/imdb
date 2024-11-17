package com.lobox.imdb.service.importer.impl;

import com.lobox.imdb.service.importer.AbstractImporter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TitleRatingsImporter extends AbstractImporter {

    @Override
    protected void saveBatch(List<CSVRecord> records) {
        String mainTable = "title_ratings";
        List<String> mainColumns = List.of("tconst", "average_rating", "num_votes");

        List<List<Object>> mainData = new ArrayList<>();

        for (CSVRecord record : records) {
            mainData.add(List.of(
                    record.get("tconst"),
                    toNullString(parseDouble(record.get("averageRating"))),
                    toNullString(parseInteger(record.get("numVotes")))
            ));
        }

        if (!mainData.isEmpty()) {
            String mainSQL = generateBatchInsertSQL(mainTable, mainData, mainColumns);
            jdbcTemplate.update(mainSQL);
        }
    }

    @Override
    public String getType() {
        return "title_ratings";
    }
}
