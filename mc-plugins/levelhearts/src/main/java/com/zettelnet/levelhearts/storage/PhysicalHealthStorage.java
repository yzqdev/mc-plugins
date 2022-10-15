package com.zettelnet.levelhearts.storage;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PhysicalHealthStorage implements HealthStorage {

    private final boolean respawn;

    public PhysicalHealthStorage() {
        this(false);
    }

    /**
     * Constructs a new HealthStorage using the Bukkit API.
     *
     * @param respawn whether players can be respawned by setting their health to a
     *                positive value when previously dead (health = 0)
     * @see Player#setHealth(double)
     * @see Player#setMaxHealth(double)
     */
    public PhysicalHealthStorage(boolean respawn) {
        this.respawn = respawn;
    }

    @Override
    public boolean isRespawn() {
        return respawn;
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public boolean terminate() {
        return true;
    }

    @Override
    public boolean loadPlayer(Player player) {
        return player.hasPlayedBefore();
    }

    @Override
    public void unloadPlayer(Player player) {
    }

    @Override
    public boolean isPlayerLoaded(Player player) {
        return true;
    }

    @Override
    public double getHealth(Player player) {
        return player.getHealth();
    }

    @Override
    public boolean setHealth(Player player, double health) {
        if (respawn || !player.isDead()) {
            if (health > getMaxHealth(player)) {
                health = getMaxHealth(player);
            }
            player.setHealth(health);
        }
        return true;
    }

    @Override
    public double getMaxHealth(Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    @Override
    public boolean setMaxHealth(Player player, double maxHealth) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        return true;
    }
}
