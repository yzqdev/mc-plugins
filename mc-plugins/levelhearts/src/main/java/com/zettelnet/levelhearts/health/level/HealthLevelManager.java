package com.zettelnet.levelhearts.health.level;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HealthLevelManager {

    private HealthLevelLoader defaultLoader;
    private final Map<String, HealthLevelLoader> loaders;

    private final Logger log;

    public HealthLevelManager(HealthLevelLoader defaultLoader, Logger log) {
        this.defaultLoader = defaultLoader;
        this.loaders = new HashMap<>();
        this.log = log;
    }

    private String escapeId(String id) {
        id = id.toLowerCase();
        id = id.replaceAll("_", "");
        return id;
    }

    public HealthLevelLoader getDefaultLoader() {
        return defaultLoader;
    }

    public void setDefaultLoader(HealthLevelLoader defaultLoader) {
        this.defaultLoader = defaultLoader;
    }

    public void addLoader(String id, HealthLevelLoader loader) {
        loaders.put(escapeId(id), loader);
    }

    public HealthLevelLoader getLoader(String id) {
        return loaders.get(escapeId(id));
    }

    public HealthLevel getHealthLevel(String id) {
        id = escapeId(id);

        if (!loaders.containsKey(id)) {
            log.warning(String.format("Health level mode %s not found!\nFalling back to default!", id));
            return getDefaultHealthLevel();
        }

        try {
            return loaders.get(id).load();
        } catch (HealthLevelLoadException e) {
            log.warning(String.format("Health level mode %s could not be loaded!", id));
            e.printStackTrace();
            return getDefaultHealthLevel();
        }
    }

    public HealthLevel getDefaultHealthLevel() {
        try {
            return defaultLoader.load();
        } catch (HealthLevelLoadException e) {
            log.warning("Default health level mode could not be loaded!");
            e.printStackTrace();
            return null;
        }
    }
}
