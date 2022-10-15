package com.zettelnet.levelhearts.zet.chat;

import org.bukkit.command.CommandSender;

public final class ChatMessageInstance {

	private final ChatMessage type;
	private final CommandSender sender;
	private final MessageValueMap values;
	private final String message;

	public ChatMessageInstance(ChatMessage type, CommandSender sender, Object... values) {
		this(type, sender, MessageValueMap.valueOf(values));
	}

	public ChatMessageInstance(ChatMessage type, CommandSender sender, MessageValueMap values) {
		this.type = type;
		this.sender = sender;
		this.values = values;
		this.message = type.format(sender, values);
	}

	@Override
	public String toString() {
		return message;
	}

	public final ChatMessage getType() {
		return type;
	}

	public final CommandSender getSender() {
		return sender;
	}

	public final MessageValueMap getValues() {
		return values;
	}

	public final String getMessage() {
		return message;
	}

	public final boolean hasMessage() {
		return message != null;
	}

	public void send() {
		if (hasMessage()) {
			sender.sendMessage(message);
		}
	}
}
