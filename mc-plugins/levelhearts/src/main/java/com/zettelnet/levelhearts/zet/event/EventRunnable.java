package com.zettelnet.levelhearts.zet.event;

import org.bukkit.event.Event;

/**
 * @author yanni
 */
public abstract class EventRunnable<T extends Event> {

    public EventRunnable() {
    }

    public abstract void run(T event);

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
