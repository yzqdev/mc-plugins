package com.zettelnet.levelhearts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.zettelnet.levelhearts.configuration.LanguageConfiguration;

public class DisabledCommands implements CommandExecutor {

	private final LanguageConfiguration lang;

	public DisabledCommands() {
		this.lang = LevelHearts.getLanguageConfiguration();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		lang.sendDisabledCommand(sender);
		return true;
	}
}
