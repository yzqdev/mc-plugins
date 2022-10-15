package com.zettelnet.levelhearts.zet.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerEvent;

import com.zettelnet.levelhearts.zet.chat.ChatMessage;
import com.zettelnet.levelhearts.zet.chat.MessageValueMap;

public class PlayerEventNotifier extends EventNotifier<PlayerEvent> {

    public PlayerEventNotifier(ChatMessage message, CommandSender sender, int mode) {
        super(message, sender, mode);
    }

    @Override
    public void run(PlayerEvent event) {
        MessageValueMap values = new MessageValueMap();
        values.put("player", event.getPlayer().getName());
        super.run(event, values);
    }
}
