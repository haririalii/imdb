package com.lobox.imdb.controller;

import com.lobox.imdb.service.importer.AbstractImporter;
import com.lobox.imdb.service.importer.ImporterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/import")
public class ImportController {

    private final ImporterFactory importerFactory;

    @PostMapping("/{model}")
    public ResponseEntity<String> importData(@PathVariable String model, @RequestParam("file") MultipartFile file) {
        try {
            AbstractImporter importer = importerFactory.getImporter(model);
            importer.importFile(file);
            return ResponseEntity.ok("Data imported successfully for model: " + model);
        } catch (IllegalArgumentException e) {
            log.error("", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return ResponseEntity.status(500).body("Unexpected error");
        }
    }
}
