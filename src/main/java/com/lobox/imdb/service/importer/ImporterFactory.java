package com.lobox.imdb.service.importer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ImporterFactory {

    private final Map<String, AbstractImporter> importers = new HashMap<>();

    private final List<AbstractImporter> importerBeans;

    @PostConstruct
    public void initialize() {
        for (AbstractImporter importer : importerBeans) {
            importers.put(importer.getType().toLowerCase(), importer);
        }
    }

    public AbstractImporter getImporter(String model) {
        AbstractImporter importer = importers.get(model.toLowerCase());
        if (importer == null) {
            throw new IllegalArgumentException("No importer found for model: " + model);
        }

        return importer;
    }
}
