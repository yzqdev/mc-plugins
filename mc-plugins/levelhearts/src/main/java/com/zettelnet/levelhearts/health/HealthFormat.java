package com.zettelnet.levelhearts.health;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.HealthFormatException;

public interface HealthFormat {

    double escapeHealth(double health, Player player);

    double parseHealth(String str, Player player) throws HealthFormatException;

    String formatHealth(double health, CommandSender sender);

    double escapeMaxHealth(double maxHealth, Player player);

    double parseMaxHealth(String str, Player player) throws HealthFormatException;

    String formatMaxHealth(double maxHealth, CommandSender sender);

}
