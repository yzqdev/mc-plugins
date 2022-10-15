package com.zettelnet.levelhearts.zet.chat;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

public class MessageValueMap extends HashMap<String, CharSequence> {
    @Serial
	private static final long serialVersionUID = 1L;

    private Map<CommandSenderType, Map<String, String>> messageValues;

    public MessageValueMap() {
        messageValues = new HashMap<CommandSenderType, Map<String, String>>();
        for (CommandSenderType type : CommandSenderType.values()) {
            messageValues.put(type, new HashMap<String, String>());
        }
    }

    @Override
    public CharSequence put(String key, CharSequence value) {
        if (value instanceof FormatOption) {
            FormatOption f = (FormatOption) value;
            for (CommandSenderType type : CommandSenderType.values()) {
                messageValues.get(type).put(key, f.toString(type));
            }
        } else {
            String str = value.toString();
            for (CommandSenderType type : CommandSenderType.values()) {
                messageValues.get(type).put(key, str);
            }
        }
        return super.put(key, value);
    }

    @Override
    public CharSequence remove(Object key) {
        messageValues.remove(key);
        return super.remove(key);
    }

    public void recalculate() {
        messageValues = new HashMap<CommandSenderType, Map<String, String>>();
        for (CommandSenderType type : CommandSenderType.values()) {
            messageValues.put(type, new HashMap<String, String>());
        }

        for (Map.Entry<String, CharSequence> entry : entrySet()) {
            String key = entry.getKey();
            CharSequence charSequence = entry.getValue();

            if (charSequence instanceof FormatOption) {
                FormatOption f = (FormatOption) charSequence;
                for (CommandSenderType type : CommandSenderType.values()) {
                    messageValues.get(type).put(key, f.toString(type));
                }
            } else {
                String value = charSequence.toString();
                for (CommandSenderType type : CommandSenderType.values()) {
                    messageValues.get(type).put(key, value);
                }
            }
        }
    }

    public Map<CommandSenderType, Map<String, String>> getMessageValues() {
        return messageValues;
    }

    public Map<String, String> getMessageValues(CommandSenderType senderType) {
        return messageValues.get(senderType);
    }

    public Map<String, String> getMessageValues(CommandSender sender) {
        return getMessageValues(CommandSenderType.valueOf(sender));
    }

    public static MessageValueMap valueOf(Map<String, String> formatOptions) {
        MessageValueMap map = new MessageValueMap();
        for (Map.Entry<String, String> entry : formatOptions.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static MessageValueMap valueOf(Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("Value array has to have an even number of elements.");
        }

        Map<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < values.length; i = i + 2) {
            map.put(values[i].toString(), values[i + 1].toString());
        }

        return valueOf(map);
    }
}
