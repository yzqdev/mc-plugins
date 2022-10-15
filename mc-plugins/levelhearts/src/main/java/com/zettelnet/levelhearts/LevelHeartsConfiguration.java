package com.zettelnet.levelhearts;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.configuration.MainConfiguration;
import com.zettelnet.levelhearts.health.HealthConfiguration;
import com.zettelnet.levelhearts.health.HealthPermissions;

public class LevelHeartsConfiguration implements HealthConfiguration {

    private final MainConfiguration config;
    private final HealthPermissions perms;

    public LevelHeartsConfiguration(MainConfiguration config, HealthPermissions perms) {
        this.config = config;
        this.perms = perms;
    }

    @Override
    public double getMaxHealthBase(Player player) {
        return config.maxHealthDefault();
    }

    @Override
    public int getMaxHealthLevelInterval(Player player) {
        if (perms.canIncreaseOnLevelUp(player)) {
            return config.maxHealthLevelInterval();
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxHealthStartLevel(Player player) {
        return config.maxHealthStartLevel();
    }

    @Override
    public double getMaxHealthInterval(Player player) {
        return 2;
    }

    @Override
    public double getMaxHealthLimit(Player player) {
        if (!perms.canBypassMaxHealthLimit(player)) {
            return config.maxHealthLimit();
        } else {
            return 280;
        }
    }
}
