package com.zettelnet.levelhearts.storage;

import java.io.Serial;

/**
 * @author yanni
 */
public class HealthStorageLoadException extends Exception {
    @Serial
    private static final long serialVersionUID = 0L;

    public HealthStorageLoadException() {
        super();
    }

    public HealthStorageLoadException(String msg) {
        super(msg);
    }

    public HealthStorageLoadException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public HealthStorageLoadException(Throwable cause) {
        super(cause);
    }
}
