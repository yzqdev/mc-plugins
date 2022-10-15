package com.zettelnet.levelhearts.health.level.exp;

import org.bukkit.plugin.Plugin;

import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.health.level.HealthLevelLoader;

/**
 * Represents a {@link HealthLevelLoader} for a {@link ExpHealthLevel}.
 * 
 * @author Zettelkasten
 * 
 */
public class ExpHealthLevelLoader implements HealthLevelLoader {

	private final Plugin plugin;
	private final HealthManager healthManager;

	public ExpHealthLevelLoader(Plugin plugin, HealthManager healthManager) {
		this.plugin = plugin;
		this.healthManager = healthManager;
	}

	@Override
	public ExpHealthLevel load() {
		return new ExpHealthLevel(plugin, healthManager);
	}
}
