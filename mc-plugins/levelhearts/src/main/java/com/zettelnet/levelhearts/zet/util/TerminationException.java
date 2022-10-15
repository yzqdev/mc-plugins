package com.zettelnet.levelhearts.zet.util;

import java.io.Serial;

public class TerminationException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 0L;

	public TerminationException() {
		super();
	}

	public TerminationException(String msg) {
		super(msg);
	}

	public TerminationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TerminationException(Throwable cause) {
		super(cause);
	}
}
