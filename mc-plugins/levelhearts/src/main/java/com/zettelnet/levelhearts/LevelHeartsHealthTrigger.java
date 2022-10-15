package com.zettelnet.levelhearts;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.zettelnet.levelhearts.configuration.LanguageConfiguration;
import com.zettelnet.levelhearts.configuration.MainConfiguration;
import com.zettelnet.levelhearts.event.PlayerMaxHealthChangeEvent;
import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.health.HealthTrigger;
import com.zettelnet.levelhearts.zet.event.EventNotifier;

public class LevelHeartsHealthTrigger implements HealthTrigger {

	private final HealthManager healthManager;

	private final MainConfiguration config;
	private final LanguageConfiguration lang;

	private final LevelHeartsPermissions perm;

	public LevelHeartsHealthTrigger(Plugin plugin, HealthManager healthManager) {
		this.healthManager = healthManager;

		this.config = LevelHearts.getConfiguration();

		this.lang = LevelHearts.getLanguageConfiguration();
		this.perm = LevelHearts.getPermissionManager();
	}

	@Override
	public void onLevelChange(final Player player, int oldLevel, int newLevel) {
		boolean increaseOnLevelUp = perm.hasMaxHealthIncreaseOnLevelUp(player);
		boolean increase = 0 <= newLevel - oldLevel;

		if (increase && !increaseOnLevelUp) {
			return;
		}

		// recalc max health
		// this may decrease it if reset on level change is enabled
		// (config.maxHealthResetLevelChange() == true)
		PlayerMaxHealthChangeEvent event = healthManager.recalculateMaxHealth(player);
		event.setIncreaseOnly(!config.maxHealthResetLevelChange());

		if (config.chatChangeMessagesEnabled()) {
			event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(lang.MaxHealthChangeLevelUp, EventNotifier.MODE_ONLY_SUCCESS | PlayerMaxHealthChangeEvent.Notifier.MODE_INCREASE_ONLY));
		}
	}

	@Override
	public void onPlayerJoin(final Player player) {
		// recalc maxhealth;
		// reset if should reset on login (config.maxHealthResetLogin() == true)

		PlayerMaxHealthChangeEvent recalcEvent = healthManager.recalculateMaxHealth(player);

		boolean reset = config.maxHealthResetLogin() && !perm.hasMaxHealthResetBypassLogin(player);
		recalcEvent.setIncreaseOnly(!reset);

		if (reset && config.chatChangeMessagesEnabled()) {
			recalcEvent.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(lang.MaxHealthChangeLogin, EventNotifier.MODE_ONLY_SUCCESS | PlayerMaxHealthChangeEvent.Notifier.MODE_DECREASE_ONLY));
		}
	}

	@Override
	public void onPlayerQuit(Player player) {
	}

	@Override
	public void onPlayerRespawn(Player player) {
		PlayerMaxHealthChangeEvent recalcEvent = healthManager.recalculateMaxHealth(player);

		boolean reset = config.maxHealthResetDeath() && !perm.hasMaxHealthResetBypassDeath(player);
		recalcEvent.setIncreaseOnly(!reset);

		if (reset && config.chatChangeMessagesEnabled()) {
			recalcEvent.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(lang.MaxHealthChangeDeath, EventNotifier.MODE_ONLY_SUCCESS | PlayerMaxHealthChangeEvent.Notifier.MODE_DECREASE_ONLY));
		}
	}
}
