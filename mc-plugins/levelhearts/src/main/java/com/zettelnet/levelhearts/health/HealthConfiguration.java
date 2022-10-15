package com.zettelnet.levelhearts.health;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.health.level.HealthLevel;

/**
 * Represents a configuration interface that supplies values.
 * <p>
 * All methods that use a {@link Player} argument can also be called with
 * <code>null</code>. In that case a player independent value will be returned.
 *
 * @author Zettelkasten
 */
public interface HealthConfiguration {

    /**
     * Returns the max health a {@link Player} has on {@link HealthLevel}
     * <code>0</code>.
     *
     * @param player the player or <code>null</code> if a player-independent value
     *               should be returned
     * @return the max health base
     */
    double getMaxHealthBase(Player player);

    /**
     * Returns the {@link HealthLevel} interval in which the {@link Player}'s
     * max health will increase by the max health interval (after the start
     * level has been reached). The level interval may be any positive number
     * including <code>0</code>, if the max health should not increase by
     * levels.
     *
     * @param player the player or <code>null</code> if a player-independent value
     *               should be returned
     * @return the level interval the max health will change on
     * @see #getMaxHealthInterval(Player)
     * @see #getMaxHealthStartLevel(Player)
     */
    int getMaxHealthLevelInterval(Player player);

    /**
     * Returns the {@link HealthLevel}, which has to be reached so that max
     * health will increase.
     *
     * @param player the player or <code>null</code> if a player-independent value
     *               should be returned
     * @return the level after which max health will change every few levels
     * @see #getMaxHealthLevelInterval(Player)
     */
    int getMaxHealthStartLevel(Player player);

    /**
     * Returns the max health interval by which the {@link Player}'s max health
     * will increase every level interval. The max health interval may be any
     * number, including negative, if the max health should decrease by levels,
     * or <code>0</code>, if the max health should not increase by levels.
     *
     * @param player the player or <code>null</code> if a player-independent value
     *               should be returned
     * @return the max health interval the max health will increase by
     * @see #getMaxHealthLevelInterval(Player)
     * @see #getMaxHealthLimit(Player)
     */
    double getMaxHealthInterval(Player player);

    /**
     * Returns the limit up to which players can increase their max health. If
     * the max health interval is negative, this value is the lower bound of the
     * max health (in other words the max health will decrease up to this
     * value).
     *
     * @param player the player or <code>null</code> if a player-independent value
     *               should be returned
     * @return the max health limit to which the max health will increase to
     * @see #getMaxHealthInterval(Player)
     */
    double getMaxHealthLimit(Player player);
}
