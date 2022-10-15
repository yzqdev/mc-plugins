package com.zettelnet.levelhearts.health.level.constant;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.health.level.HealthLevel;

public class ConstantHealthLevel implements HealthLevel {

	private final int level;
	
	public ConstantHealthLevel(int level) {
		this.level = level;
	}
	
	@Override
	public int get(Player player) {
		return level;
	}

	@Override
	public String getIdentifier() {
		return "constant";
	}
}
