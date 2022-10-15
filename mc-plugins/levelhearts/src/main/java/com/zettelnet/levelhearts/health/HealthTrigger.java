package com.zettelnet.levelhearts.health;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.health.level.HealthLevel;

/**
 * Represents a health trigger that is called when a player's
 * {@link HealthLevel} changes.
 * 
 * @author Zettelkasten
 * 
 */
public interface HealthTrigger {

	/**
	 * Called when a player's {@link HealthLevel} changes. This should be used
	 * by implementations to update the max health of the player according to
	 * the new level.
	 * 
	 * @param player
	 *            the player
	 * @param oldLevel
	 *            the old health level
	 * @param newLevel
	 *            the new health level
	 */
	void onLevelChange(Player player, int oldLevel, int newLevel);

	/**
	 * Called when a player joins the server. This should be used by
	 * implementations to update the max health according to the event.
	 * 
	 * @param player
	 *            the player that joined
	 */
	void onPlayerJoin(Player player);

	/**
	 * Called when a player quits the server. This should be used by
	 * implementations to update the max health according to the event.
	 * 
	 * @param player
	 *            the player that quit
	 */
	void onPlayerQuit(Player player);

	/**
	 * Called when a player respawns. This should be used by implementations to
	 * update the max health according to the event.
	 * 
	 * @param player
	 *            the player that respawned
	 */
	void onPlayerRespawn(Player player);
}
