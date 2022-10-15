package com.zettelnet.levelhearts.storage;

/**
 * @author yanni
 */
public class PhysicalHealthStorageFactory implements HealthStorageFactory {

    private boolean respawn;

    public PhysicalHealthStorageFactory() {
        this(false);
    }

    /**
     * Constructs a factory for {@link PhysicalHealthStorage}.
     *
     * @param respawn whether players can be respawned by setting their health to a
     *                positive value when previously dead (health = 0)
     * @see PhysicalHealthStorage#PhysicalHealthStorage(boolean)
     */
    public PhysicalHealthStorageFactory(boolean respawn) {
        this.respawn = respawn;
    }

    @Override
    public HealthStorage makeStorage() {
        return new PhysicalHealthStorage(respawn);
    }
}
