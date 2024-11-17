package com.lobox.imdb.service.importer;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractImporter {

    protected static final int BATCH_SIZE = 100000;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected abstract void saveBatch(List<CSVRecord> records);

    public abstract String getType();

    @Transactional
    public void importFile(MultipartFile file) throws Exception {
        List<CSVRecord> batch = new ArrayList<>(BATCH_SIZE);

        log.info("Starting import for type: {}", getType());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             org.apache.commons.csv.CSVParser csvParser = new org.apache.commons.csv.CSVParser(reader, org.apache.commons.csv.CSVFormat.TDF.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .setRecordSeparator("\n")
                     .setEscape('\\')
                     .setQuote(null)
                     .build())) {

            long recordCount = 0;

            for (CSVRecord record : csvParser) {
                recordCount++;
                batch.add(record);

                if (batch.size() >= BATCH_SIZE) {
                    saveBatch(new ArrayList<>(batch));
                    batch.clear();
                }

                if (recordCount % 100000 == 0) {
                    log.info("{} records processed so far...", recordCount);
                }
            }

            if (!batch.isEmpty()) {
                saveBatch(batch);
            }

            log.info("All records have been processed. Total records: {}", recordCount);
        }
    }

    protected String generateBatchInsertSQL(String tableName, List<List<Object>> records, List<String> columns) {
        if (records.isEmpty() || columns.isEmpty()) {
            throw new IllegalArgumentException("Records and columns cannot be empty for batch insert.");
        }

        String columnList = String.join(", ", columns);

        String values = records.stream()
                .map(record -> "(" +
                        record.stream()
                                .map(this::formatValue)
                                .collect(Collectors.joining(", "))
                        + ")")
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES %s;", tableName, columnList, values);
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof String && !value.equals("NULL")) {
            return "'" + value.toString().replace("'", "''") + "'";
        }
        return value.toString();
    }

    protected Integer parseInteger(String value) {
        return "\\N".equals(value) || value.isEmpty() ? null : Integer.parseInt(value);
    }

    protected Double parseDouble(String value) {
        return "\\N".equals(value) || value.isEmpty() ? null : Double.parseDouble(value);
    }

    protected Object toNullString(Object value) {
        return value == null ? "NULL" : value;
    }
}
