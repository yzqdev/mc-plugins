package com.zettelnet.levelhearts.zet.util;

import java.io.Serial;

public class InitializationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 0L;

    public InitializationException() {
        super();
    }

    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }
}
