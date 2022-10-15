package com.zettelnet.levelhearts.zet.chat;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public enum CommandSenderType {

	Player, Console, Block;

	public static CommandSenderType valueOf(CommandSender sender) {
		if (sender instanceof Player) {
			return Player;
		}
		if (sender instanceof ConsoleCommandSender) {
			return Console;
		}
		if (sender instanceof BlockCommandSender) {
			return Block;
		}
		return null;
	}
}
