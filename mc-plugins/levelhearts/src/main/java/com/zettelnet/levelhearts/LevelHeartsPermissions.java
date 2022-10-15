package com.zettelnet.levelhearts;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.zettelnet.levelhearts.health.HealthPermissions;
import com.zettelnet.levelhearts.zet.permissions.PermissionManager;

public class LevelHeartsPermissions extends PermissionManager implements HealthPermissions {

	public LevelHeartsPermissions() {
		super(LevelHearts.getPlugin());
	}

	public void loadValues(FileConfiguration config) {
		
		// maxHealth
		add("maxHealthChangeable", new Permission(config.getString("permissions.maxHealth.changeable", "lvlhearts.maxhealth.changeable"),
				"Your max health is changeable.", PermissionDefault.TRUE));
		add("maxHealthIncreaseOnLevelUp", new Permission(config.getString("permissions.maxHealth.increaseOnLevelUp", "lvlhearts.maxhealth.increaselevel"),
				"You can increase your maxhealth by leveling up.", PermissionDefault.TRUE));
		add("maxHealthBypassLimit", new Permission(config.getString("permissions.maxHealth.bypassLimit", "lvlhearts.maxhealth.nolimit"),
				"You can bypass the maxhealth limit", PermissionDefault.FALSE));
		add("maxHealthResetBypassLogin", new Permission(config.getString("permissions.maxHealth.reset.bypassLogin", "lvlhearts.maxhealth.reset.login.bypass"),
				"Your can bypass resetting your maxhealth when you login.", PermissionDefault.FALSE));
		add("maxHealthResetBypassDeath", new Permission(config.getString("permissions.maxHealth.reset.bypassDeath", "lvlhearts.maxhealth.reset.death.bypass"),
				"Your can bypass resetting your maxhealth when you die.", PermissionDefault.FALSE));

		// health
		add("healthChangeable", new Permission(config.getString("permissions.health.changeable", "lvlhearts.health.changeable"), "Your health is changeable.",
				PermissionDefault.TRUE));
		/*
		 * only if configuration option "maxhealth.restoreHealth" is enabled set
		 * default permission to TRUE, otherwise set it to FALSE
		 */
		add("healthRestore", new Permission(config.getString("permissions.health.restore", "lvlhearts.health.restore"),
				"Your health is restored when you change your maxhealth.", LevelHearts.getConfiguration().maxHealthRestoreHealth() ? PermissionDefault.TRUE
						: PermissionDefault.FALSE));

		// COMMANDS

		// maxHealth
		add("cmdMaxHealthEnabled", new Permission(config.getString("permissions.commands.maxHealth.enabled", "lvlhearts.maxhealth.command"),
				"You can use the /maxhealth command.", PermissionDefault.TRUE));
		add("cmdMaxHealthGetOthers", new Permission(config.getString("permissions.commands.maxHealth.getOthers", "lvlhearts.maxhealth.get.others"),
				"You can get the maxhealth of other permissibles.", PermissionDefault.OP));
		add("cmdMaxHealthSetOwn", new Permission(config.getString("permissions.commands.maxHealth.setOwn", "lvlhearts.maxhealth.set"),
				"You can change your maxhealth.", PermissionDefault.OP));
		add("cmdMaxHealthSetOthers", new Permission(config.getString("permissions.commands.maxHealth.setOthers", "lvlhearts.maxhealth.set.others"),
				"You can change the maxhealth of other permissibles."));

		// health
		add("cmdHealthEnabled", new Permission(config.getString("permissions.commands.health.enabled", "lvlhearts.health.command"),
				"You can use the /health command.", PermissionDefault.TRUE));
		add("cmdHealthGetOthers", new Permission(config.getString("permissions.commands.health.getOthers", "lvlhearts.health.get.others"),
				"You can get the health of other permissibles.", PermissionDefault.OP));
		add("cmdHealthSetOwn", new Permission(config.getString("permissions.commands.setOwn", "lvlhearts.health.set"), "You can change your health.",
				PermissionDefault.OP));
		add("cmdHealthSetOthers", new Permission(config.getString("permissions.commands.setOthers", "lvlhearts.health.set.others"),
				"You can change the health of other permissibles."));

		// info
		add("cmdInfoEnabled", new Permission(config.getString("permissions.commands.info.enabled", "lvlhearts.info.command"),
				"You can you use the /lvlhearts command.", PermissionDefault.TRUE));
		add("cmdInfoReload", new Permission(config.getString("permissions.commands.info.reload", "lvlhearts.info.reload"), "You can reload LevelHearts.",
				PermissionDefault.OP));
	}

	public boolean hasMaxHealthChangeable(Permissible permissible) {
		return hasPermission(permissible, "maxHealthChangeable");
	}

	public boolean hasMaxHealthIncreaseOnLevelUp(Permissible permissible) {
		return hasPermission(permissible, "maxHealthIncreaseOnLevelUp");
	}

	public boolean hasMaxHealthBypassLimit(Permissible permissible) {
		return hasPermission(permissible, "maxHealthBypassLimit");
	}

	public boolean hasMaxHealthResetBypassLogin(Permissible permissible) {
		return hasPermission(permissible, "maxHealthResetBypassLogin");
	}

	public boolean hasMaxHealthResetLogin(Permissible permissible) {
		return LevelHearts.getConfiguration().maxHealthResetLogin() && !hasMaxHealthResetBypassLogin(permissible);
	}

	public boolean hasMaxHealthResetBypassDeath(Permissible permissible) {
		return hasPermission(permissible, "maxHealthResetBypassDeath");
	}

	public boolean hasMaxHealthResetDeath(Permissible permissible) {
		return LevelHearts.getConfiguration().maxHealthResetDeath() && !hasMaxHealthResetBypassDeath(permissible);
	}

	public boolean hasHealthChangeable(Permissible permissible) {
		return hasPermission(permissible, "healthChangeable");
	}

	public boolean hasHealthRestore(Permissible permissible) {
		return hasPermission(permissible, "healthRestore");
	}

	public boolean hasCommandMaxHealthEnabled(Permissible permissible) {
		return hasPermission(permissible, "cmdMaxHealthEnabled");
	}

	public boolean hasCommandMaxHealthGetOthers(Permissible permissible) {
		return hasPermission(permissible, "cmdMaxHealthGetOthers");
	}

	public boolean hasCommandMaxHealthSetOwn(Permissible permissible) {
		return hasPermission(permissible, "cmdMaxHealthSetOwn");
	}

	public boolean hasCommandMaxHealthSetOthers(Permissible permissible) {
		return hasPermission(permissible, "cmdMaxHealthSetOthers");
	}

	public boolean hasCommandHealthEnabled(Permissible permissible) {
		return hasPermission(permissible, "cmdHealthEnabled");
	}

	public boolean hasCommandHealthGetOthers(Permissible permissible) {
		return hasPermission(permissible, "cmdHealthGetOthers");
	}

	public boolean hasCommandHealthSetOwn(Permissible permissible) {
		return hasPermission(permissible, "cmdHealthSetOwn");
	}

	public boolean hasCommandHealthSetOthers(Permissible permissible) {
		return hasPermission(permissible, "cmdHealthSetOthers");
	}

	public boolean hasCommandInfoEnabled(Permissible permissible) {
		return hasPermission(permissible, "cmdInfoEnabled");
	}

	public boolean hasCommandInfoReload(Permissible permissible) {
		return hasPermission(permissible, "cmdInfoReload");
	}

	@Override
	public boolean canChangeHealth(Permissible permissible) {
		if (permissible != null) {
			return hasHealthChangeable(permissible);
		} else {
			return true;
		}
	}

	@Override
	public boolean canChangeMaxHealth(Permissible permissible) {
		if (permissible != null) {
			return hasMaxHealthChangeable(permissible);
		} else {
			return true;
		}
	}

	@Override
	public boolean canRestoreHealth(Permissible permissible) {
		if (permissible != null) {
			return hasHealthRestore(permissible);
		} else {
			return true;
		}
	}

	@Override
	public boolean canIncreaseOnLevelUp(Permissible permissible) {
		if (permissible != null) {
			return hasMaxHealthIncreaseOnLevelUp(permissible);
		} else {
			return true;
		}
	}

	@Override
	public boolean canBypassMaxHealthLimit(Permissible permissible) {
		if (permissible != null) {
			return hasMaxHealthBypassLimit(permissible);
		} else {
			return false;
		}
	}
}
