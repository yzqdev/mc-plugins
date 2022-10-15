package com.zettelnet.levelhearts.zet.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import org.apache.commons.lang.text.StrSubstitutor;

public class FormattedChatMessage extends ChatMessage {

    private final String defaultMessage;

    private final MessageValueMap formatOptions;
    private final Map<CommandSenderType, String> formatted;

    public FormattedChatMessage(String rawMessage, String defaultRawMessage, Object... formatOptions) {
        this(rawMessage, defaultRawMessage, MessageValueMap.valueOf(formatOptions));
    }

    public FormattedChatMessage(String rawMessage, String defaultRawMessage, MessageValueMap formatOptions) {
        super(rawMessage);

        this.formatOptions = formatOptions;
        formatted = new HashMap<CommandSenderType, String>();

        if (isNullMessage()) {
            for (CommandSenderType type : CommandSenderType.values()) {
                formatted.put(type, null);
            }
            defaultMessage = null;
            return;
        }

        this.defaultMessage = getFormattedMessage(defaultRawMessage, formatOptions.getMessageValues(CommandSenderType.Console));

        Map<CommandSenderType, Map<String, String>> values = formatOptions.getMessageValues();

        for (Entry<CommandSenderType, Map<String, String>> entry : values.entrySet()) {
            CommandSenderType type = entry.getKey();
            Map<String, String> formats = entry.getValue();

            formatted.put(type, getFormattedMessage(rawMessage, formats));
        }

    }

    private String getFormattedMessage(String rawMessage, Map<String, String> formats) {
        StrSubstitutor sub = new StrSubstitutor(formats, "&(", ")", '\\');
        String format = sub.replace(rawMessage);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replaceAll("[ ]{2,}", " ");
        return format;
    }

    @Override
    public String format(CommandSenderType senderType, MessageValueMap values) {
        if (isNullMessage()) {
            return null;
        }

        // StrSubstitutor sub = new StrSubstitutor(values, "%(", ")");
        // StrSubstitutor sub = new StrSubstitutor(new
        // CaseInsensitiveStrLookup<CharSequence>(values), "%(", ")", '\\');

        StrSubstitutor sub = new StrSubstitutor(values.getMessageValues(senderType), "%(", ")", '\\');
        String format = sub.replace(getRawMessage(senderType));

        if (senderType != CommandSenderType.Player) {
            format = ChatColor.stripColor(format);
        }

        return format;
    }

    public final String getRawMessage(CommandSenderType senderType) {
        // return formatted.get(senderType);
        return senderType == CommandSenderType.Console ? defaultMessage : formatted.get(senderType);
    }

    public final String getRawMessage(CommandSender sender) {
        return getRawMessage(CommandSenderType.valueOf(sender));
    }

    public final String getRawMessageDefault() {
        return defaultMessage;
    }

    public final MessageValueMap getFormatOptions() {
        return formatOptions;
    }

    @Override
    public FormattedChatMessage mergeWith(ChatMessage other) {
        String raw = this.getRawMessage() + "\n" + other.getRawMessage();
        String rawDefault = this.getRawMessageDefault() + "\n" + other.getRawMessage();
        MessageValueMap formatOptions = (MessageValueMap) this.getFormatOptions().clone();
        if (other instanceof FormattedChatMessage) {
            FormattedChatMessage otherF = (FormattedChatMessage) other;
            formatOptions.putAll(otherF.getFormatOptions());
            rawDefault = this.getRawMessageDefault() + "\n" + otherF.getRawMessageDefault();
        }
        return new FormattedChatMessage(raw, rawDefault, formatOptions);
    }
}
