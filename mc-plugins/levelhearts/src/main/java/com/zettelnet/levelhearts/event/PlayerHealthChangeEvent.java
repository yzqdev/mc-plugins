package com.zettelnet.levelhearts.event;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.zettelnet.levelhearts.LevelHearts;
import com.zettelnet.levelhearts.health.HealthFormat;
import com.zettelnet.levelhearts.zet.chat.ChatMessage;
import com.zettelnet.levelhearts.zet.chat.MessageValueMap;
import com.zettelnet.levelhearts.zet.event.Callable;
import com.zettelnet.levelhearts.zet.event.EventNotifier;
import com.zettelnet.levelhearts.zet.event.EventRunnable;
import com.zettelnet.levelhearts.zet.event.Monitorable;

public class PlayerHealthChangeEvent extends PlayerEvent implements Cancellable, Monitorable<PlayerHealthChangeEvent> {
	private static final HandlerList handlers = new HandlerList();

	private final double oldHealth;
	private double newHealth;
	private boolean increaseOnly;

	private boolean cancelled;

	private final Set<EventRunnable<PlayerHealthChangeEvent>> monitors;

	public PlayerHealthChangeEvent(Player player, double oldHealth) {
		this(player, oldHealth, -1);
	}

	public PlayerHealthChangeEvent(Player player, double oldHealth, double newHealth) {
		super(player);

		this.oldHealth = oldHealth;
		this.newHealth = newHealth;
		this.increaseOnly = false;

		this.cancelled = false;

		this.monitors = new HashSet<>(1);
	}

	public boolean isNewHealthCalculated() {
		return newHealth != -1;
	}

	public double getNewHealth() {
		return newHealth;
	}

	public void setNewHealth(double newHealth) {
		this.newHealth = newHealth;
	}

	public double getHealthChange() {
		if (!isNewHealthCalculated()) {
			return 0;
		}
		return newHealth - oldHealth;
	}

	public boolean isHealthChanged() {
		return getHealthChange() != 0;
	}

	public double getOldHealth() {
		return oldHealth;
	}

	public boolean isIncreaseOnly() {
		return increaseOnly;
	}

	public void setIncreaseOnly(boolean increaseOnly) {
		this.increaseOnly = increaseOnly;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public void addMonitor(EventRunnable<PlayerHealthChangeEvent> runnable) {
		monitors.add(runnable);
	}

	@Override
	public Set<EventRunnable<PlayerHealthChangeEvent>> getMonitors() {
		return monitors;
	}

	public static class Notifier extends EventNotifier<PlayerHealthChangeEvent> {

		private final ChatMessage statusMessage;

		public Notifier(ChatMessage message, int mode) {
			this(message, mode, LevelHearts.getLanguageConfiguration().HealthStatus);
		}

		public Notifier(ChatMessage message, int mode, ChatMessage statusMessage) {
			super(message, mode);
			this.statusMessage = statusMessage;
		}

		public Notifier(ChatMessage message, CommandSender sender, int mode) {
			this(message, sender, mode, LevelHearts.getLanguageConfiguration().HealthStatus);
		}

		public Notifier(ChatMessage message, CommandSender sender, int mode, ChatMessage statusMessage) {
			super(message, sender, mode);
			this.statusMessage = statusMessage;
		}

		@Override
		public void run(PlayerHealthChangeEvent event) {
			if (getSender() == null) {
				setSender(event.getPlayer());
			}
			CommandSender sender = getSender();

			HealthFormat healthFormat = LevelHearts.getHealthFormat();
			MessageValueMap values = new MessageValueMap();
			values.put("player", event.getPlayer().getName());
			values.put("oldHealth", healthFormat.formatHealth(event.getOldHealth(), sender));
			values.put("health", healthFormat.formatHealth(event.getNewHealth(), sender));
			values.put("healthChange", healthFormat.formatHealth(event.getHealthChange(), sender));
			values.put("status", statusMessage.format(sender, (MessageValueMap) values.clone()));
			run(event, values);
		}
	}


}
