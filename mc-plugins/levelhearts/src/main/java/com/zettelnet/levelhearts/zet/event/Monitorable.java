package com.zettelnet.levelhearts.zet.event;

import java.util.Set;
import org.bukkit.event.Event;

public interface Monitorable<T extends Event> {

	void addMonitor(EventRunnable<T> runnable);

	Set<EventRunnable<T>> getMonitors();
}
