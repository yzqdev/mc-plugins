package com.zettelnet.levelhearts;

public class HealthFormatException extends RuntimeException {
	private static final long serialVersionUID = 0L;

	public HealthFormatException() {
		super();
	}

	public HealthFormatException(String msg) {
		super(msg);
	}

	public HealthFormatException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public HealthFormatException(Throwable cause) {
		super(cause);
	}
}
