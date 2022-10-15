package com.zettelnet.levelhearts.storage;

public interface HealthStorageFactory {

	HealthStorage makeStorage() throws HealthStorageLoadException;
}
