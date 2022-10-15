package com.zettelnet.levelhearts.health;

import org.bukkit.permissions.Permissible;

public interface HealthPermissions {

	boolean canChangeHealth(Permissible permissible);

	boolean canChangeMaxHealth(Permissible permissible);

	boolean canRestoreHealth(Permissible permissible);

	boolean canIncreaseOnLevelUp(Permissible permissible);

	boolean canBypassMaxHealthLimit(Permissible permissible);
}
