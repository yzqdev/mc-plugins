package com.zettelnet.levelhearts.zet.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

public class FormatOption implements CharSequence {

    private final Map<CommandSenderType, String> values;
    private final String defaultValue;

    public FormatOption(String playerValue) {
        values = new HashMap<>();
        values.put(CommandSenderType.Player, playerValue);

        this.defaultValue = playerValue;
    }

    public FormatOption(String playerValue, String consoleValue) {
        this(playerValue);
        values.put(CommandSenderType.Console, consoleValue);
    }

    public FormatOption(String playerValue, String consoleValue, String blockValue) {
        this(playerValue);
        values.put(CommandSenderType.Console, consoleValue);
        values.put(CommandSenderType.Block, blockValue);
    }

    public FormatOption(Map<CommandSenderType, String> values) {
        defaultValue = values.get(CommandSenderType.Player);
        this.values = values;
    }

    @Override
    public char charAt(int index) {
        return defaultValue.charAt(index);
    }

    @Override
    public int length() {
        return defaultValue.length();
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return defaultValue.subSequence(beginIndex, endIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return other.toString().equals(this.toString());
    }

    @Override
    public String toString() {
        return defaultValue;
    }

    public String toString(CommandSenderType senderType) {
        return values.getOrDefault(senderType, defaultValue);
    }

    public String toString(CommandSender sender) {
        return toString(CommandSenderType.valueOf(sender));
    }
}
