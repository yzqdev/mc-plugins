package com.zettelnet.levelhearts.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zettelnet.levelhearts.LevelHeartsPlugin;
import com.zettelnet.levelhearts.storage.sql.SQLConnector;
import com.zettelnet.levelhearts.storage.sql.SQLHealthStorage;

public class SQLiteHealthStorageFactory implements HealthStorageFactory, SQLConnector {

    private final LevelHeartsPlugin plugin;

    public SQLiteHealthStorageFactory(LevelHeartsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * sqlite连接
     * @return HealthStorage
     * @throws HealthStorageLoadException
     */
    @Override
    public HealthStorage makeStorage() throws HealthStorageLoadException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new HealthStorageLoadException("SQLite driver not found.", e);
        }

        SQLHealthStorage storage = new SQLHealthStorage(this, plugin.getConfiguration().storageTable(), plugin.getLogger());

        try {
            storage.testConnection();
        } catch (SQLException e) {
            throw new HealthStorageLoadException("Connection is invalid", e);
        }

        return storage;
    }

    @Override
    public Connection createConnection() throws SQLException {
        File dir = plugin.getDataFolder();
        dir.mkdirs();
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s", new File(dir, "data.db")));
    }
}
