package com.zettelnet.levelhearts.health.level;

public class HealthLevelLoadException extends Exception {
	private static final long serialVersionUID = 0L;

	public HealthLevelLoadException() {
		super();
	}

	public HealthLevelLoadException(String msg) {
		super(msg);
	}

	public HealthLevelLoadException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public HealthLevelLoadException(Throwable cause) {
		super(cause);
	}
}
