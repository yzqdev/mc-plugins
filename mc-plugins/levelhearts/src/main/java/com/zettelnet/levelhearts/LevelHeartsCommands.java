package com.zettelnet.levelhearts;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.configuration.LanguageConfiguration;
import com.zettelnet.levelhearts.event.PlayerHealthChangeEvent;
import com.zettelnet.levelhearts.event.PlayerMaxHealthChangeEvent;
import com.zettelnet.levelhearts.health.HealthFormat;
import com.zettelnet.levelhearts.health.HealthManager;
import com.zettelnet.levelhearts.zet.chat.ChatMessage;
import com.zettelnet.levelhearts.zet.chat.ChatMessageInstance;

public class LevelHeartsCommands implements CommandExecutor {

	/**
	 * Wraps {@link Bukkit#getPlayer(String)} in this method. It is totally fine
	 * to call {@link Bukkit#getPlayer(String)} when you are dealing with
	 * players currently online and it's the only way to get an online player by
	 * its name.
	 */
	@SuppressWarnings("deprecation")
	private static Player getPlayerByName(String playerName) {
		return Bukkit.getPlayer(playerName);
	}

	private final HealthManager hm;
	private final HealthFormat hf;

	private final LevelHeartsPermissions perm;
	private final LanguageConfiguration lang;

	public LevelHeartsCommands() {
		this.hm = LevelHearts.getHealthManager();
		this.hf = LevelHearts.getHealthFormat();

		this.perm = LevelHearts.getPermissionManager();
		this.lang = LevelHearts.getLanguageConfiguration();
	}

	/*
	 * health [player]
	 * 
	 * health help
	 * 
	 * health get [player]
	 * 
	 * health set [player] <amount>
	 * 
	 * health give [player] <amount>
	 * 
	 * health take [player] <amount>
	 * 
	 * health restore [player]
	 * 
	 * 
	 * maxhealth [player]
	 * 
	 * maxhealth help
	 * 
	 * maxhealth get [player]
	 * 
	 * maxhealth set [player] <amount>
	 * 
	 * maxhealth give [player] <amount>
	 * 
	 * 
	 * 
	 * maxhealth restore [player]
	 * 
	 * levelhearts levelhearts help levelhearts version levelhearts reload
	 */

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String command = cmd.getName().toLowerCase();

		String[] argsNull = Arrays.copyOf(args, 3);

		if (command.equals("health")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					return onHealthGet(sender, "health [player]", null);
				} else {
					return onHealthGet(sender, "health <player>", null);
				}
			}

			switch (args[0].toLowerCase()) {
			case "help":
				return onHealthHelp(sender, "health help");
			case "get":
				if (sender instanceof Player) {
					return onHealthGet(sender, "health get [player]", argsNull[1]);
				} else {
					return onHealthGet(sender, "health get <player>", argsNull[1]);
				}
			case "set":
			case "change":
				if (sender instanceof Player) {
					return onHealthSet(sender, "health set [player] <amount>", argsNull[1], argsNull[2]);
				} else {
					return onHealthSet(sender, "health set <player> <amount>", argsNull[1], argsNull[2]);
				}
			case "give":
			case "add":
				if (sender instanceof Player) {
					return onHealthGive(sender, "health give [player] <amount>", argsNull[1], argsNull[2]);
				} else {
					return onHealthGive(sender, "health give <player> <amount>", argsNull[1], argsNull[2]);
				}
			case "take":
			case "remove":
			case "rem":
			case "substract":
				if (sender instanceof Player) {
					return onHealthTake(sender, "health take [player] <amount>", argsNull[1], argsNull[2]);
				} else {
					return onHealthTake(sender, "health take <player> <amount>", argsNull[1], argsNull[2]);
				}
			case "restore":
			case "reset":
			case "replenish":
			case "fill":
			case "full":
			case "complete":
				if (sender instanceof Player) {
					return onHealthRestore(sender, "health restore [player]", argsNull[1]);
				} else {
					return onHealthRestore(sender, "health restore <player>", argsNull[1]);
				}
			}
			if (sender instanceof Player) {
				return onHealthGet(sender, "health [player]", argsNull[0]);
			} else {
				return onHealthGet(sender, "health <player>", argsNull[0]);
			}
		}

		// maxhealth
		if (command.equals("maxhealth")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					return onMaxHealthGet(sender, "maxhealth [player]", null);
				} else {
					return onMaxHealthGet(sender, "maxhealth <player>", null);
				}
			}

			switch (args[0].toLowerCase()) {
			case "help":
				return onMaxHealthHelp(sender, "maxhealth help");
			case "get":
				if (sender instanceof Player) {
					return onMaxHealthGet(sender, "maxhealth get [player]", argsNull[1]);
				} else {
					return onMaxHealthGet(sender, "maxhealth get <player>", argsNull[1]);
				}
			case "set":
			case "change":
				if (sender instanceof Player) {
					return onMaxHealthSet(sender, "maxhealth set [player] <amount>", argsNull[1], argsNull[2]);
				} else {
					return onMaxHealthSet(sender, "maxhealth set <player> <amount>", argsNull[1], argsNull[2]);
				}
			case "give":
			case "add":
				if (sender instanceof Player) {
					return onMaxHealthGive(sender, "maxhealth give [player] <amount>", argsNull[1], argsNull[2]);
				} else {
					return onMaxHealthGive(sender, "maxhealth give <player> <amount>", argsNull[1], argsNull[2]);
				}
			case "take":
			case "remove":
			case "rem":
			case "substract":
				if (sender instanceof Player) {
					return onMaxHealthTake(sender, "maxhealth take [player] <amount>", argsNull[1], argsNull[2]);
				} else {
					return onMaxHealthTake(sender, "maxhealth take <player> <amount>", argsNull[1], argsNull[2]);
				}
			case "reset":
			case "restore":
			case "replenish":
			case "recalculate":
			case "recalc":
				if (sender instanceof Player) {
					return onMaxHealthReset(sender, "maxhealth restore [player]", argsNull[1]);
				} else {
					return onMaxHealthReset(sender, "maxhealth restore <player>", argsNull[1]);
				}
			}
			if (sender instanceof Player) {
				return onMaxHealthGet(sender, "maxhealth [player]", argsNull[0]);
			} else {
				return onMaxHealthGet(sender, "maxhealth <player>", argsNull[0]);
			}
		}

		if (command.equals("levelhearts")) {
			if (args.length == 0) {
				return onLevelHearts(sender, "levelhearts");
			}

			switch (args[0].toLowerCase()) {
			case "help":
				return onLevelHeartsHelp(sender, "levelhearts help");
			case "reload":
			case "rl":
				return onLevelHeartsReload(sender, "levelhearts reload");
			}
			return onErrorInvalidArgument(sender, "levelhearts help", argsNull[0]);
		}
		return false;
	}

	// /\\\ ERRORS ///\\\

	public boolean onError(CommandSender sender, ChatMessageInstance message) {
		message.send();
		return true;
	}

	public boolean onError(CommandSender sender, ChatMessageInstance message, String syntax) {
		message.send();
		lang.CommandErrorSyntax.send(sender, "syntax", syntax);
		return true;
	}

	public boolean onErrorInvalidArgument(CommandSender sender, String syntax, String argument) {
		return onError(sender, lang.CommandErrorInvalidArgument.instance(sender, "arg", argument), syntax);
	}

	public boolean onErrorInvalidArgument(CommandSender sender, String syntax, ChatMessage type, String argument) {
		return onError(sender, lang.CommandErrorInvalidArgumentType.instance(sender, "arg", argument, "argType", type.getMessage(sender)), syntax);
	}

	public boolean onErrorMissingArgument(CommandSender sender, String syntax, ChatMessage type) {
		return onError(sender, lang.CommandErrorMissingArgument.instance(sender, "argType", type.getMessage(sender)), syntax);
	}

	public boolean onErrorNotPlayer(CommandSender sender, String syntax, String playerName) {
		return onError(sender, lang.CommandErrorNotPlayer.instance(sender, "player", playerName), syntax);
	}

	public boolean onErrorNotNumber(CommandSender sender, String syntax, String numberStr) {
		return onError(sender, lang.CommandErrorNotNumber.instance(sender, "number", numberStr), syntax);
	}

	public boolean onErrorNotPositiveNumber(CommandSender sender, String syntax, String numberStr) {
		return onError(sender, lang.CommandErrorNotPositiveNumber.instance(sender, "number", numberStr), syntax);
	}

	public boolean onErrorNoPermission(CommandSender sender, String syntax) {
		return onError(sender, lang.CommandErrorNoPermission.instance(sender));
	}

	// /\\\ CHECKS ///\\\

	private boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public boolean isNotValidPlayer(CommandSender sender, String syntax, String playerStr) {
		if (isEmpty(playerStr)) {
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
		}
		Player player = getPlayerByName(playerStr);

		if (player == null) {
			return onErrorNotPlayer(sender, syntax, playerStr);
		}
		return false;
	}

	public boolean isNotValidHealth(CommandSender sender, String syntax, String healthStr) {
		return isNotValidHealth(sender, syntax, healthStr, true);
	}

	public boolean isNotValidHealth(CommandSender sender, String syntax, String healthStr, boolean allowZero) {
		if (isEmpty(healthStr)) {
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
		}
		double health;
		try {
			health = Double.parseDouble(healthStr) * 2;
		} catch (NumberFormatException e) {
			return onErrorNotPositiveNumber(sender, syntax, healthStr);
		}
		if (health < 1 || allowZero && health < 0) {
			return onErrorNotPositiveNumber(sender, syntax, healthStr);
		}
		return false;
	}

	public boolean isNotValidMaxHealth(CommandSender sender, String syntax, String maxHealthStr) {
		if (isEmpty(maxHealthStr)) {
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
		}
		double maxHealth;
		try {
			maxHealth = Double.parseDouble(maxHealthStr) * 2;
		} catch (NumberFormatException e) {
			return onErrorNotPositiveNumber(sender, syntax, maxHealthStr);
		}
		if (maxHealth < 1) {
			return onErrorNotPositiveNumber(sender, syntax, maxHealthStr);
		}
		return false;
	}

	// /\\\ HEALTH ///\\\

	// health help
	public boolean onHealthHelp(CommandSender sender, String syntax) {
		lang.sendHealthCommandHelp(sender);
		return true;
	}

	// health get [player]
	public boolean onHealthGet(CommandSender sender, String syntax, String arg0) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandHealthEnabled(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				double health = hm.getHealth(player);
				double maxHealth = hm.getMaxHealth(player);
				int nextMaxHealthIncreaseLevel = hm.getHealthCalculator().getMaxHealthChangeLevel(maxHealth, player);
				lang.sendHealthInfoSelf(sender, player, health, maxHealth, nextMaxHealthIncreaseLevel);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
		}

		if (sender instanceof Player && !perm.hasCommandHealthGetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		double health = hm.getHealth(target);
		double maxHealth = hm.getMaxHealth(target);
		int nextMaxHealthIncreaseLevel = hm.getHealthCalculator().getMaxHealthChangeLevel(maxHealth, target);
		lang.sendHealthInfoOther(sender, target, health, maxHealth, nextMaxHealthIncreaseLevel);
		return true;
	}

	// health set [player] <amount>
	public boolean onHealthSet(CommandSender sender, String syntax, String arg0, String arg1) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
			} else {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
			}
		}

		if (isEmpty(arg1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				if (isNotValidHealth(sender, syntax, arg0)) {
					return true;
				}
				double health = hf.parseHealth(arg0, player);
				PlayerHealthChangeEvent event = hm.setHealth(player, health);
				lang.sendCommandHealthSet(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
		}

		if (sender instanceof Player && !perm.hasCommandHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		if (isNotValidHealth(sender, syntax, arg1)) {
			return true;
		}
		double health = hf.parseHealth(arg1, target);
		PlayerHealthChangeEvent event = hm.setHealth(target, health);
		lang.sendCommandHealthSet(event, sender, false);
		return true;
	}

	// health give [player] <amount>
	public boolean onHealthGive(CommandSender sender, String syntax, String arg0, String arg1) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
			} else {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
			}
		}

		if (isEmpty(arg1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				if (isNotValidHealth(sender, syntax, arg0, false)) {
					return true;
				}
				double health = hf.escapeHealth(hf.parseHealth(arg0, player) + hm.getHealth(player), player);
				PlayerHealthChangeEvent event = hm.setHealth(player, health);
				lang.sendCommandHealthGive(event, sender, true);

				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
		}

		if (sender instanceof Player && !perm.hasCommandHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		if (isNotValidHealth(sender, syntax, arg1, false)) {
			return true;
		}
		double health = hf.escapeHealth(hf.parseHealth(arg1, target) + hm.getHealth(target), target);
		PlayerHealthChangeEvent event = hm.setHealth(target, health);
		lang.sendCommandHealthGive(event, sender, false);
		return true;
	}

	// health take [player] <amount>
	public boolean onHealthTake(CommandSender sender, String syntax, String arg0, String arg1) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
			} else {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
			}
		}

		if (isEmpty(arg1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				if (isNotValidHealth(sender, syntax, arg0)) {
					return true;
				}
				double health = hf.escapeHealth(hm.getHealth(player) - hf.parseHealth(arg0, player), player);
				PlayerHealthChangeEvent event = hm.setHealth(player, health);
				lang.sendCommandHealthTake(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentHealth);
		}

		if (sender instanceof Player && !perm.hasCommandHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		if (isNotValidHealth(sender, syntax, arg1)) {
			return true;
		}
		double health = hf.escapeHealth(hm.getHealth(target) - hf.parseHealth(arg1, target), target);
		PlayerHealthChangeEvent event = hm.setHealth(target, health);
		lang.sendCommandHealthTake(event, sender, false);
		return true;
	}

	// health restore [player]
	public boolean onHealthRestore(CommandSender sender, String syntax, String arg0) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				PlayerHealthChangeEvent event = hm.restoreHealth(player);
				lang.sendCommandHealthRestore(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
		}

		if (sender instanceof Player && !perm.hasCommandHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		PlayerHealthChangeEvent event = hm.restoreHealth(target);
		lang.sendCommandHealthRestore(event, sender, false);
		return true;
	}

	// /\\\ MAXHEALTH ///\\\

	// maxhealth help
	public boolean onMaxHealthHelp(CommandSender sender, String syntax) {
		lang.sendMaxHealthCommandHelp(sender);
		return true;
	}

	// maxhealth get [player]
	public boolean onMaxHealthGet(CommandSender sender, String syntax, String arg0) {
		return onHealthGet(sender, syntax, arg0);
	}

	// maxhealth set [player] <amount>
	public boolean onMaxHealthSet(CommandSender sender, String syntax, String arg0, String arg1) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
			} else {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
			}
		}

		if (isEmpty(arg1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandMaxHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				if (isNotValidMaxHealth(sender, syntax, arg0)) {
					return true;
				}
				double maxHealth = hf.parseMaxHealth(arg0, player);
				PlayerMaxHealthChangeEvent event = hm.setMaxHealth(player, maxHealth);
				lang.sendCommandMaxHealthSet(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
		}

		if (sender instanceof Player && !perm.hasCommandMaxHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		if (isNotValidMaxHealth(sender, syntax, arg1)) {
			return true;
		}
		double maxHealth = hf.parseMaxHealth(arg1, target);
		PlayerMaxHealthChangeEvent event = hm.setMaxHealth(target, maxHealth);
		lang.sendCommandMaxHealthSet(event, sender, false);
		return true;
	}

	// maxhealth give [player] <amount>
	public boolean onMaxHealthGive(CommandSender sender, String syntax, String arg0, String arg1) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
			} else {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
			}
		}

		if (isEmpty(arg1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandMaxHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				if (isNotValidMaxHealth(sender, syntax, arg0)) {
					return true;
				}
				double maxHealth = hf.escapeMaxHealth(hf.parseMaxHealth(arg0, player) + hm.getMaxHealth(player), player);
				PlayerMaxHealthChangeEvent event = hm.setMaxHealth(player, maxHealth);
				lang.sendCommandMaxHealthGive(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
		}

		if (sender instanceof Player && !perm.hasCommandMaxHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		if (isNotValidMaxHealth(sender, syntax, arg1)) {
			return true;
		}
		double maxHealth = hf.escapeMaxHealth(hf.parseMaxHealth(arg1, target) + hm.getMaxHealth(target), target);
		PlayerMaxHealthChangeEvent event = hm.setMaxHealth(target, maxHealth);
		lang.sendCommandMaxHealthGive(event, sender, false);
		return true;
	}

	// maxhealth take [Player] amount
	public boolean onMaxHealthTake(CommandSender sender, String syntax, String arg0, String arg1) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
			} else {
				return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
			}
		}
		if (isEmpty(arg1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandMaxHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				if (isNotValidMaxHealth(sender, syntax, arg0)) {
					return true;
				}
				double maxHealth = hf.escapeMaxHealth(hm.getMaxHealth(player) - hf.parseMaxHealth(arg0, player), player);
				if (maxHealth < 1) {
					maxHealth = 1;
				}
				PlayerMaxHealthChangeEvent event = hm.setMaxHealth(player, maxHealth);
				lang.sendCommandMaxHealthTake(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentMaxHealth);
		}

		if (sender instanceof Player && !perm.hasCommandMaxHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		if (isNotValidMaxHealth(sender, syntax, arg1)) {
			return true;
		}
		double maxHealth = hf.escapeMaxHealth(hm.getMaxHealth(target) - hf.parseMaxHealth(arg1, target), target);
		if (maxHealth < 1) {
			maxHealth = 1;
		}
		PlayerMaxHealthChangeEvent event = hm.setMaxHealth(target, maxHealth);
		lang.sendCommandMaxHealthTake(event, sender, false);
		return true;
	}

	// maxhealth reset [player]
	public boolean onMaxHealthReset(CommandSender sender, String syntax, String arg0) {
		if (isEmpty(arg0)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!perm.hasCommandMaxHealthSetOwn(player)) {
					return onErrorNoPermission(sender, syntax);
				}
				PlayerMaxHealthChangeEvent event = hm.recalculateMaxHealth(player);
				lang.sendCommandMaxHealthReset(event, sender, true);
				return true;
			}
			return onErrorMissingArgument(sender, syntax, lang.CommandArgumentPlayer);
		}

		if (sender instanceof Player && !perm.hasCommandMaxHealthSetOthers((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		if (isNotValidPlayer(sender, syntax, arg0)) {
			return true;
		}
		Player target = getPlayerByName(arg0);
		PlayerMaxHealthChangeEvent event = hm.recalculateMaxHealth(target);
		lang.sendCommandMaxHealthReset(event, sender, false);
		return true;
	}

	// /\\\ LEVELHEARTS ///\\\

	// levelhearts
	public boolean onLevelHearts(CommandSender sender, String syntax) {
		if (sender instanceof Player && !perm.hasCommandInfoEnabled((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}
		lang.sendPluginInformation(sender);
		return true;
	}

	// levelhearts help
	public boolean onLevelHeartsHelp(CommandSender sender, String syntax) {
		lang.sendPluginCommandHelp(sender);
		return true;
	}

	// levelhearts reload
	public boolean onLevelHeartsReload(CommandSender sender, String syntax) {
		if (sender instanceof Player && !perm.hasCommandInfoReload((Player) sender)) {
			return onErrorNoPermission(sender, syntax);
		}

		LevelHearts.getPlugin().reload();
		lang.sendPluginReload(sender);
		return true;
	}
}
