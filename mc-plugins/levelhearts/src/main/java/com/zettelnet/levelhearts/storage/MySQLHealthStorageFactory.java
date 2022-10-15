package com.zettelnet.levelhearts.storage;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zettelnet.levelhearts.LevelHeartsPlugin;
import com.zettelnet.levelhearts.configuration.MainConfiguration;
import com.zettelnet.levelhearts.storage.sql.SQLConnector;
import com.zettelnet.levelhearts.storage.sql.SQLHealthStorage;

public class MySQLHealthStorageFactory implements HealthStorageFactory, SQLConnector {

    private final LevelHeartsPlugin plugin;

    public MySQLHealthStorageFactory(LevelHeartsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public HealthStorage makeStorage() throws HealthStorageLoadException {
        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new HealthStorageLoadException("MySQL driver not found.", e);
        }

        MainConfiguration config = plugin.getConfiguration();
        SQLHealthStorage storage = new SQLHealthStorage(this, config.storageTable(), plugin.getLogger());

        try {
            storage.testConnection();
        } catch (SQLException e) {
            throw new HealthStorageLoadException("Connection is invalid", e);
        }

        return storage;
    }

    @Override
    public Connection createConnection() throws SQLException {
        MainConfiguration config = plugin.getConfiguration();
        return DriverManager.getConnection(String.format("jdbc:mysql://%s", config.storageDatabase()), config.storageUsername(), config.storagePassword());
    }
}
