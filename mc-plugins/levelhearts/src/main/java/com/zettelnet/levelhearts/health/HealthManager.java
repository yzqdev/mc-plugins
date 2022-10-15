package com.zettelnet.levelhearts.health;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.event.PlayerHealthChangeEvent;
import com.zettelnet.levelhearts.event.PlayerMaxHealthChangeEvent;
import com.zettelnet.levelhearts.health.level.HealthLevel;

public interface HealthManager {

    HealthCalculator getHealthCalculator();

    HealthConfiguration getHealthConfiguration();

    HealthPermissions getHealthPermissions();

    HealthLevel getHealthLevel();

    void setHealthLevel(HealthLevel level);

    HealthTrigger getHealthTrigger();

    void setHealthTrigger(HealthTrigger trigger);

    double getHealth(Player player);

    double getMaxHealth(Player player);

    PlayerHealthChangeEvent setHealth(Player player, double health);

    PlayerHealthChangeEvent setHealthAsynchronously(Player player, HealthCallable health);

    PlayerMaxHealthChangeEvent setMaxHealth(Player player, double maxHealth);

    PlayerMaxHealthChangeEvent setMaxHealthAsynchronously(Player player, HealthCallable maxHealth);

    PlayerHealthChangeEvent restoreHealth(Player player);

    PlayerMaxHealthChangeEvent recalculateMaxHealth(Player player);
}
