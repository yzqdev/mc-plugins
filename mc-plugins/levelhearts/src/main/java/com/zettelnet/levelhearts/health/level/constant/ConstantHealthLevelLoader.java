package com.zettelnet.levelhearts.health.level.constant;

import com.zettelnet.levelhearts.health.level.HealthLevel;
import com.zettelnet.levelhearts.health.level.HealthLevelLoader;


public class ConstantHealthLevelLoader implements HealthLevelLoader {

	private final int level;

	public ConstantHealthLevelLoader(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public HealthLevel load() {
		return new ConstantHealthLevel(level);
	}

}
