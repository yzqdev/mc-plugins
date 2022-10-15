package com.zettelnet.levelhearts;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.health.HealthCalculator;
import com.zettelnet.levelhearts.health.HealthConfiguration;

/**计算等级提升的血量
 * @author yanni
 */
public class LevelHeartsHealthCalculator implements HealthCalculator {

    private final HealthConfiguration config;

    public LevelHeartsHealthCalculator(HealthConfiguration healthConfig) {
        this.config = healthConfig;
    }

    @Override
    public int getLevelForMaxHealth(double maxHealth, Player player) {
        double maxHealthInterval = config.getMaxHealthInterval(player);

        // we need to differentiate whether max health increases or decreases
        if (maxHealthInterval >= 0) {
            // if that max health is unreachable, return -1
            if (maxHealth > config.getMaxHealthLimit(player)) {
                return -1;
            }
            // if that max health is already given with level 0, return 0
            if (maxHealth <= config.getMaxHealthBase(player)) {
                return 0;
            }
        } else {
            // if that max health is unreachable, return -1
            if (maxHealth < config.getMaxHealthLimit(player)) {
                return -1;
            }
            // if that max health is already given with level 0, return 0
            if (maxHealth >= config.getMaxHealthBase(player)) {
                return 0;
            }
        }

        // level = (maxHealth - base) / maxHealthInterval * levelInterval + startLevel
        // e.g. level = (maxHealth - 20) / 2 * 5 + startLevel
        int level = (int) (((maxHealth - config.getMaxHealthBase(player)) / config.getMaxHealthInterval(player)) * config.getMaxHealthLevelInterval(player)) + config.getMaxHealthStartLevel(player);
        return level > 0 ? level : 0;
    }

    @Override
    public double getMaxHealthAtLevel(int level, Player player) {
        // if the level interval is 0, you will stay at your base maxHealth
        if (config.getMaxHealthLevelInterval(player) == 0) {
            return config.getMaxHealthBase(player);
        }

        // maxHealth = base + maxHealthInterval * floor(max(level - startLevel; 0) / levelInterval)
        // e.g. maxHealth = 20 + 2 * floor(level / 5)
        double maxHealth = config.getMaxHealthBase(player) + config.getMaxHealthInterval(player) * (int) (Math.max(level - config.getMaxHealthStartLevel(player), 0) / config.getMaxHealthLevelInterval(player));

        // max health has to be contained in limit
        if (maxHealth > config.getMaxHealthLimit(player)) {
            return config.getMaxHealthLimit(player);
        }
        return maxHealth;
    }

    @Override
    public int getMaxHealthChangeLevel(double maxHealth, Player player) {
        return getLevelForMaxHealth(maxHealth + config.getMaxHealthInterval(player), player);
    }
}
