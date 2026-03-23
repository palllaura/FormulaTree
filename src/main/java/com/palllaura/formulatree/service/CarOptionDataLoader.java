package com.palllaura.formulatree.service;

import com.palllaura.formulatree.dto.CarOptionJson;
import com.palllaura.formulatree.entity.CarOption;
import com.palllaura.formulatree.repository.CarOptionRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads initial CarOption data from JSON file into database.
 * Builds hierarchical structure and calculates levels automatically.
 */
@Service
public class CarOptionDataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarOptionDataLoader.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CarOptionRepository repository;
    private final EntityManager entityManager;

    public CarOptionDataLoader(CarOptionRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadIfEmpty();
    }

    /**
     * Loads data only if database is empty.
     */
    public void loadIfEmpty() {
        if (repository.count() > 0) {
            LOGGER.info("Car options already present; skipping load.");
            return;
        }

        List<CarOptionJson> jsonList = loadJson();
        Map<String, CarOption> optionMap = createOptions(jsonList);

        linkHierarchy(jsonList, optionMap);
        calculateLevels(optionMap);
        persistAll(optionMap);

        LOGGER.info("Loaded {} car options from JSON.", optionMap.size());
    }

    /**
     * Reads JSON file from resources and maps it into DTO list.
     */
    List<CarOptionJson> loadJson() {
        ClassPathResource resource = new ClassPathResource("data/car-options.json");

        if (!resource.exists()) {
            throw new IllegalStateException("car-options.json not found: " + resource.getPath());
        }

        try (InputStream is = resource.getInputStream()) {
            return MAPPER.readValue(is, new TypeReference<>() {});
        } catch (Exception e) {
            throw new DataLoadingException("Failed to read car-options.json", e);
        }
    }

    /**
     * Creates CarOption entities without relationships.
     */
    Map<String, CarOption> createOptions(List<CarOptionJson> jsonList) {
        Map<String, CarOption> map = new LinkedHashMap<>();

        for (CarOptionJson json : jsonList) {
            CarOption option = new CarOption();
            option.setName(json.getName());
            map.put(json.getKey(), option);
        }

        return map;
    }

    /**
     * Links parent-child relationships between CarOptions.
     */
    void linkHierarchy(List<CarOptionJson> jsonList, Map<String, CarOption> map) {
        for (CarOptionJson json : jsonList) {
            String parentKey = json.getParent();

            if (parentKey != null) {
                CarOption child = map.get(json.getKey());
                CarOption parent = map.get(parentKey);

                if (parent == null) {
                    LOGGER.warn("Parent {} not found for {}", parentKey, json.getKey());
                } else {
                    child.setParent(parent);
                    parent.getChildren().add(child);
                }
            }
        }
    }

    /**
     * Calculates hierarchy depth (level) for each CarOption.
     */
    void calculateLevels(Map<String, CarOption> map) {
        for (CarOption option : map.values()) {
            option.setLevel(calculateLevel(option));
        }
    }

    /**
     * Calculates level by traversing parent chain.
     */
    private int calculateLevel(CarOption option) {
        int level = 0;
        CarOption current = option.getParent();

        while (current != null) {
            level++;
            current = current.getParent();
        }

        return level;
    }

    /**
     * Persists all CarOptions into database.
     */
    void persistAll(Map<String, CarOption> map) {
        for (CarOption option : map.values()) {
            entityManager.persist(option);
        }
        entityManager.flush();
    }

    /**
     * Data loading exception.
     */
    public static class DataLoadingException extends RuntimeException {
        public DataLoadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}