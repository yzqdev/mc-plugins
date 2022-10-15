package com.zettelnet.levelhearts.health.level;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.health.HealthCalculator;

/**
 * Represents a getter interface for health levels. Health levels are used to
 * calculate max health values by {@link HealthCalculator}s.
 * <p>
 * <strong>All health level methods should be implemented thread-safe and do not
 * access any Bukkit API.</strong>
 * 
 * @author Zettelkasten
 * 
 */
public interface HealthLevel {

	/**
	 * Returns the current health level of a {@link Player}.
	 * <p>
	 * <strong>Health levels should always be called asynchronously!</strong>
	 * 
	 * @param player
	 *            the player
	 * @return the current health level
	 */
	int get(Player player);

	/**
	 * Returns the identifier of this type of health level, used to address this
	 * HealthLevel in e.g. the healthLevel metrics chart ("exp", "concrete",
	 * etc.).
	 * 
	 * @return the identifier of this HealthLevel type
	 */
	String getIdentifier();
}
