package com.zettelnet.levelhearts.storage;

import org.bukkit.entity.Player;

/**
 * @author yanni
 */
public interface HealthStorage {

    boolean initialize();

    boolean terminate();

    /**
     * @return whether the player has played before
     */
    boolean loadPlayer(Player player);

    void unloadPlayer(Player player);

    boolean isPlayerLoaded(Player player);

    boolean isRespawn();

    double getHealth(Player player);

    boolean setHealth(Player player, double health);

    double getMaxHealth(Player player);

    boolean setMaxHealth(Player player, double maxHealth);


}
