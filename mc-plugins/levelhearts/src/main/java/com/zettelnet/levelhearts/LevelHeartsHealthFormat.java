package com.zettelnet.levelhearts;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.configuration.LanguageConfiguration;
import com.zettelnet.levelhearts.health.HealthFormat;
import com.zettelnet.levelhearts.health.HealthManager;

/**
 * @author yanni
 */
public class LevelHeartsHealthFormat implements HealthFormat {

    private final HealthManager healthManager;

    private final LanguageConfiguration lang;

    public LevelHeartsHealthFormat(HealthManager healthManager, LanguageConfiguration lang) {
        this.healthManager = healthManager;
        this.lang = lang;
    }

    @Override
    public double escapeHealth(double health, Player player) {
        health = health - health % 1;
        if (health < 0) {
            return 0;
        }
        if (health > healthManager.getMaxHealth(player)) {
            return healthManager.getMaxHealth(player);
        }
        return health;
    }

    @Override
    public double parseHealth(String str, Player player) throws HealthFormatException {
        try {
            double health = Double.parseDouble(str);
            health *= 2;
            health = health - health % 1;
            return escapeHealth(health, player);
        } catch (NumberFormatException e) {
            throw new HealthFormatException(e);
        }
    }

    @Override
    public String formatHealth(double health, CommandSender sender) {
        health = Math.round(health) / 2D;
        String healthStr = health == (int) health ? String.valueOf((int) health) : String.valueOf(health);
        return lang.HealthDisplay.getMessage(sender, "health", healthStr);
    }

    @Override
    public double escapeMaxHealth(double maxHealth, Player player) {
        maxHealth = maxHealth - maxHealth % 1;
        if (maxHealth < 0) {
            return 0;
        }
        if (maxHealth > healthManager.getHealthConfiguration().getMaxHealthLimit(player)) {
            return healthManager.getHealthConfiguration().getMaxHealthLimit(player);
        }
        return maxHealth;
    }

    @Override
    public double parseMaxHealth(String str, Player player) throws HealthFormatException {
        double maxHealth = Double.parseDouble(str);
        maxHealth *= 2;
        maxHealth = maxHealth - maxHealth % 1;
        return escapeMaxHealth(maxHealth, player);
    }

    @Override
    public String formatMaxHealth(double maxHealth, CommandSender sender) {
        maxHealth = Math.round(maxHealth) / 2D;
        String healthStr = maxHealth == (int) maxHealth ? String.valueOf((int) maxHealth) : String.valueOf(maxHealth);
        return lang.MaxHealthDisplay.getMessage(sender, "maxHealth", healthStr);
    }

}
