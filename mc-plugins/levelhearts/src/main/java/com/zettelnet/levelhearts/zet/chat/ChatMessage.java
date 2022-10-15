package com.zettelnet.levelhearts.zet.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.apache.commons.lang.text.StrSubstitutor;

public class ChatMessage {

	public final static ChatMessage NONE = new ChatMessage(null);

	private final String rawMessage;
	private final boolean nullMessage;

	public ChatMessage(String rawMessage) {
		this.rawMessage = escapeCharacters(rawMessage);
		this.nullMessage = checkNullMessage();
	}

	protected boolean checkNullMessage() {
		return rawMessage == null || rawMessage.equalsIgnoreCase("null") || rawMessage.equalsIgnoreCase("none");
	}

	public String format(CommandSenderType senderType, MessageValueMap values) {
		if (nullMessage) {
			return null;
		}

		// StrSubstitutor sub = new
		// StrSubstitutor(values.getMessageValues(senderType), "%(", ")");
		StrSubstitutor sub = new StrSubstitutor(values.getMessageValues(senderType), "%(", ")", '\\');
		String format = sub.replace(rawMessage);

		if (senderType != CommandSenderType.Player) {
			format = ChatColor.stripColor(format);
		}

		return format;
	}

	public String format(CommandSenderType senderType, Object... values) {
		return format(senderType, MessageValueMap.valueOf(values));
	}

	public String format(CommandSender sender, MessageValueMap values) {
		return format(CommandSenderType.valueOf(sender), values);
	}

	public String format(CommandSender sender, Object... values) {
		return format(sender, MessageValueMap.valueOf(values));
	}

	public final String getRawMessage() {
		return rawMessage;
	}

	public final boolean isNullMessage() {
		return nullMessage;
	}

	public String getMessage(CommandSender sender, MessageValueMap values) {
		return instance(sender, values).getMessage();
	}

	public String getMessage(CommandSender sender, Object... values) {
		return getMessage(sender, MessageValueMap.valueOf(values));
	}

	public ChatMessageInstance instance(CommandSender sender, MessageValueMap values) {
		return new ChatMessageInstance(this, sender, values);
	}

	public ChatMessageInstance instance(CommandSender sender, Object... values) {
		return instance(sender, MessageValueMap.valueOf(values));
	}

	public void send(CommandSender sender, MessageValueMap values) {
		new ChatMessageInstance(this, sender, values).send();
	}

	public void send(CommandSender sender, Object... values) {
		send(sender, MessageValueMap.valueOf(values));
	}

	@Override
	public String toString() {
		return rawMessage;
	}

	public FormatOption toFormatOption(Object... values) {
		return toFormatOption(MessageValueMap.valueOf(values));
	}

	public FormatOption toFormatOption(MessageValueMap values) {
		Map<CommandSenderType, String> formatTypes = new HashMap<CommandSenderType, String>();
		for (Entry<CommandSenderType, Map<String, String>> entry : values.getMessageValues().entrySet()) {
			CommandSenderType senderType = entry.getKey();
			formatTypes.put(senderType, format(senderType, values));
		}
		return new FormatOption(formatTypes);
	}
	
	public ChatMessage mergeWith(ChatMessage other) {
		return new ChatMessage(this.getRawMessage() + "\n" + other.getRawMessage());
	}

	private String escapeCharacters(String str) {
		if (str == null) {
			return null;
		}
		char[] cArray = str.toCharArray();
		for (int i = 0; i < cArray.length; i++) {
			// cyrillic symbols fix
			char c = cArray[i];
			if (c >= 1040 && c < 1004) {
				cArray[i] -= 848;
			} else if (c == 1105) {
				cArray[i] = 184;
			}
		}
		return new String(cArray);
	}
}
