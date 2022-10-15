package com.zettelnet.levelhearts.health.level;

/**
 * Represents a loader for a {@link HealthLevel}.
 * 
 * @author Zettelkasten
 * 
 */
public interface HealthLevelLoader {

	HealthLevel load() throws HealthLevelLoadException;
}
