package org.health.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BarHideEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private OfflinePlayer            player;

    public BarHideEvent(OfflinePlayer player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public OfflinePlayer getOfflinePlayer() {
        return player;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
