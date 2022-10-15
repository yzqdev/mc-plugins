package com.zettelnet.levelhearts.health;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.zettelnet.levelhearts.health.level.HealthLevel;

/**
 * Represents a health calculator that calculates max health according to
 * {@link HealthLevel}s.
 * <p>
 * <strong>All health calculator methods should be implemented thread-safe and
 * do not access any Bukkit API.</strong>
 * 
 * @author Zettelkasten
 * 
 */
public interface HealthCalculator {

	/**
	 * Calculates the max health a {@link Player} will have at a certain level.
	 * <p>
	 * If a specific player is supplied calculations will take
	 * {@link Permission}s and other player specific properties into account.
	 * 
	 * @param level
	 *            the {@link HealthLevel} value of the player
	 * @param player
	 *            the player or <code>null</code> if the calculation should be
	 *            player-independent
	 * @return the max health value the player will have at the supplied level
	 * @see #getLevelForMaxHealth(double, Player)
	 */
	double getMaxHealthAtLevel(int level, Player player);

	/**
	 * Calculates the level a {@link Player} will need to have a certain max
	 * health.
	 * <p>
	 * If a specific player is supplied calculations will take
	 * {@link Permission}s and other player specific properties into account.
	 * 
	 * @param maxHealth
	 *            the max health value to reach
	 * @param player
	 *            the player or <code>null</code> if the level should be
	 *            player-independent
	 * @return the smallest level the player will have this max health at or
	 *         <code>-1</code>, if that max health value is unreachable. If the
	 *         max health decreases per level interval, the <u>largest</u> level
	 *         will be returned.
	 * @see #getMaxHealthAtLevel(int, Player)
	 * @see #getMaxHealthChangeLevel(double, Player)
	 */
	int getLevelForMaxHealth(double maxHealth, Player player);

	/**
	 * Calculates the level a {@link Player}'s max health will change at next.
	 * <p>
	 * If a specific player is supplied calculations will take
	 * {@link Permission}s and other player specific properties into account.
	 * 
	 * @param maxHealth
	 *            the max health value the player currently has
	 * @param player
	 *            the player or <code>null</code> if the level should be
	 *            player-independent
	 * @return the level the max health will change on
	 * @see #getLevelForMaxHealth(double, Player)
	 */
	int getMaxHealthChangeLevel(double maxHealth, Player player);
}
