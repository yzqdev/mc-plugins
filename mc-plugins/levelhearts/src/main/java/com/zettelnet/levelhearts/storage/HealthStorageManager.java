package com.zettelnet.levelhearts.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HealthStorageManager {

    private HealthStorageFactory defaultFactory;
    private final Map<String, HealthStorageFactory> factories;

    private final Logger log;

    public HealthStorageManager(HealthStorageFactory defaultFactory, Logger log) {
        this.defaultFactory = defaultFactory;
        this.factories = new HashMap<>();
        this.log = log;
    }

    private String escapeId(String id) {
        id = id.toLowerCase();
        id = id.replaceAll("_", "");
        return id;
    }

    public HealthStorageFactory getDefaultFactory() {
        return defaultFactory;
    }

    public void setDefaultFactory(HealthStorageFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public void addFactory(String id, HealthStorageFactory factory) {
        factories.put(escapeId(id), factory);
    }

    public HealthStorageFactory getFactory(String id) {
        return factories.get(escapeId(id));
    }

    /**
     * 获取storage id  {sqlite或者mysql}
     * @param id storage id
     * @return HealthStorage
     */
    public HealthStorage makeStorage(String id) {
        id = escapeId(id);

        if (!factories.containsKey(id)) {
            log.severe(String.format("Health storage mode %s not found!\nFalling back to default!", id));
            return makeDefaultStorage();
        }

        try {
            return factories.get(id).makeStorage();
        } catch (HealthStorageLoadException e) {
            log.severe(String.format("Health storage mode %s could not be loaded!", id));
            e.printStackTrace();
            return makeDefaultStorage();
        }
    }

    public HealthStorage makeDefaultStorage() {
        try {
            return defaultFactory.makeStorage();
        } catch (HealthStorageLoadException e) {
            log.severe("Default storage mode could not be loaded!");
            e.printStackTrace();
            return null;
        }
    }
}
