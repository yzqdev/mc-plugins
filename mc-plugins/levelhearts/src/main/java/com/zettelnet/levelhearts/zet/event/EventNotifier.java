package com.zettelnet.levelhearts.zet.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.zettelnet.levelhearts.zet.chat.ChatMessage;
import com.zettelnet.levelhearts.zet.chat.MessageValueMap;

public abstract class EventNotifier<T extends Event> extends EventRunnable<T> {

    private final ChatMessage message;
    private CommandSender sender;
    private final int mode;

    public final static int MODE_ALWAYS = 0x1 << 0;
    public final static int MODE_ONLY_SUCCESS = 0x1 << 1;
    public final static int MODE_ONLY_FAILURE = 0x1 << 2;

    protected EventNotifier(ChatMessage message, int mode) {
        this(message, null, mode);
    }

    public EventNotifier(ChatMessage message, CommandSender sender, int mode) {
        this.message = message;
        this.sender = sender;
        this.mode = mode;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void setSender(CommandSender sender) {
        this.sender = sender;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public int getMode() {
        return mode;
    }

    protected boolean checkMode(T event) {
        if (!(event instanceof Cancellable)) {
            return true;
        }
        Cancellable e = (Cancellable) event;
        if ((mode & MODE_ALWAYS) > 0) {
            return true;
        }
        if ((mode & MODE_ONLY_SUCCESS) > 0 && (e.isCancelled())) {
            return false;
        }
        if ((mode & MODE_ONLY_FAILURE) > 0 && (!e.isCancelled())) {
            return false;
        }

        return true;
    }

    @Override
    public void run(T event) {
        run(event, new MessageValueMap());
    }

    protected void run(T event, MessageValueMap values) {
        if (!checkMode(event)) {
            return;
        }
        message.send(sender, values);
    }

}
