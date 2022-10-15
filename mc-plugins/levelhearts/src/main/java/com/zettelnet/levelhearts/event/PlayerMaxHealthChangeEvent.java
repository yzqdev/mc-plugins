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

/**
 * @author yanni
 */
public class PlayerMaxHealthChangeEvent extends PlayerEvent implements Cancellable, Monitorable<PlayerMaxHealthChangeEvent>  {
    private static final HandlerList handlers = new HandlerList();

    private final double oldMaxHealth;
    private double newMaxHealth;
    private boolean restoreFullHealth;

    private boolean increaseOnly;

    private boolean cancelled;

    private final Set<EventRunnable<PlayerMaxHealthChangeEvent>> monitors;

    public PlayerMaxHealthChangeEvent(Player player, double oldMaxHealth, boolean restoreFullHealth) {
        this(player, oldMaxHealth, -1, restoreFullHealth);
    }

    public PlayerMaxHealthChangeEvent(Player player, double oldMaxHealth, double newMaxHealth, boolean restoreFullHealth) {
        super(player);

        this.oldMaxHealth = oldMaxHealth;
        this.newMaxHealth = newMaxHealth;
        this.restoreFullHealth = restoreFullHealth;
        this.increaseOnly = false;

        this.cancelled = false;

        this.monitors = new HashSet<>(1);
    }

    public boolean isNewMaxHealthCalculated() {
        return newMaxHealth != -1;
    }

    public double getNewMaxHealth() {
        return newMaxHealth;
    }

    public void setNewMaxHealth(double newMaxHealth) {
        this.newMaxHealth = newMaxHealth;
    }

    public double getMaxHealthChange() {
        if (!isNewMaxHealthCalculated()) {
            return 0;
        }
        return newMaxHealth - oldMaxHealth;
    }

    public boolean isMaxHealthChanged() {
        return getMaxHealthChange() != 0;
    }

    public boolean isRestoreFullHealth() {
        return restoreFullHealth;
    }

    public void setRestoreFullHealth(boolean restoreFullHealth) {
        this.restoreFullHealth = restoreFullHealth;
    }

    public double getOldMaxHealth() {
        return oldMaxHealth;
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
    public void addMonitor(EventRunnable<PlayerMaxHealthChangeEvent> runnable) {
        monitors.add(runnable);
    }

    @Override
    public Set<EventRunnable<PlayerMaxHealthChangeEvent>> getMonitors() {
        return monitors;
    }

    // will only send message if change is positive
    public static class Notifier extends EventNotifier<PlayerMaxHealthChangeEvent> {

        public static final int MODE_INCREASE_ONLY = 0x1 << 8;
        public static final int MODE_DECREASE_ONLY = 0x1 << 9;

        private final ChatMessage statusMessage;

        public Notifier(ChatMessage message, int mode) {
            this(message, mode, LevelHearts.getLanguageConfiguration().MaxHealthStatus);
        }

        public Notifier(ChatMessage message, int mode, ChatMessage statusMessage) {
            super(message, mode);
            this.statusMessage = statusMessage;
        }

        public Notifier(ChatMessage message, CommandSender sender, int mode) {
            this(message, sender, mode, LevelHearts.getLanguageConfiguration().MaxHealthStatus);
        }

        public Notifier(ChatMessage message, CommandSender sender, int mode, ChatMessage statusMessage) {
            super(message, sender, mode);
            this.statusMessage = statusMessage;
        }

        @Override
        protected boolean checkMode(PlayerMaxHealthChangeEvent event) {
            double change = event.getMaxHealthChange();
            int mode = getMode();
            if ((mode & MODE_INCREASE_ONLY) > 0 && !(change > 0)) {
                return false;
            }
            if ((mode & MODE_DECREASE_ONLY) > 0 && !(change < 0)) {
                return false;
            }
            return super.checkMode(event);
        }

        @Override
        public void run(PlayerMaxHealthChangeEvent event) {
            if (getSender() == null) {
                setSender(event.getPlayer());
            }
            CommandSender sender = getSender();

            HealthFormat healthFormat = LevelHearts.getHealthFormat();
            MessageValueMap values = new MessageValueMap();
            values.put("player", event.getPlayer().getName());
            values.put("oldMaxHealth", healthFormat.formatMaxHealth(event.getOldMaxHealth(), sender));
            values.put("maxHealth", healthFormat.formatMaxHealth(event.getNewMaxHealth(), sender));
            values.put("maxHealthChange", healthFormat.formatMaxHealth(event.getMaxHealthChange(), sender));
            values.put("status", statusMessage.format(sender, (MessageValueMap) values.clone()));

            // only send if max health actually increased
            run(event, values);
        }
    }


}
