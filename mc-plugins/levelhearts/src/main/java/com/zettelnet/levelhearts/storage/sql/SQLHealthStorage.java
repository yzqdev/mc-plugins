package com.zettelnet.levelhearts.storage.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.storage.HealthStorage;
import com.zettelnet.levelhearts.storage.PhysicalHealthStorage;

/**
 * Represents a health storage using SQL.
 * <p>
 * This storage will perform the following operations:
 * <ul>
 * <li>On initialization, create the table if it did not exist.</li>
 * <li>On player load, query player data from the table and if no entry was
 * found, create an entry in the table.</li>
 * <li>On player update, update the player data in the table.</li>
 * </ul>
 * <p>
 * This storage will use a new {@link Connection} for every operation.
 *
 * @author Zettelkasten
 */
public class SQLHealthStorage implements HealthStorage {

    private static final String INITIALIZE_SQL = "CREATE TABLE IF NOT EXISTS `%s` (`UID` char(36) NOT NULL, `Name` varchar(16) NULL, `Health` double NOT NULL, `MaxHealth` double NOT NULL, PRIMARY KEY (`UID`));";
    private static final String PLAYER_LOAD_SQL = "SELECT `Health`, `MaxHealth` FROM `%s` WHERE `UID` = ?";
    private static final String PLAYER_CREATE_SQL = "INSERT INTO `%s` (`UID`, `Name`, `Health`, `MaxHealth`) VALUES (?, ?, ?, ?)";
    private static final String PLAYER_UPDATE_SQL = "UPDATE `%s` SET `Name` = ?, `Health` = ?, `MaxHealth` = ? WHERE `UID` = ?";

    private final SQLConnector connector;

    private final String table;
    private final Logger log;

    private final Map<Player, HealthData> offlineData;
    private final HealthStorage backedStorage;
    private final HealthStorage respawnStorage;

    private boolean initialized;

    public SQLHealthStorage(final SQLConnector connector, final String tableName, final Logger log) {
        this.connector = connector;

        this.table = tableName;
        this.log = log;

        this.offlineData = new HashMap<>();
        this.backedStorage = new PhysicalHealthStorage(false);
        this.respawnStorage = new PhysicalHealthStorage(true);

        this.initialized = false;
    }

    public void testConnection() throws SQLException {
        try (Connection con = connector.createConnection()) {
            if (con.isClosed()) {
                throw new SQLException("Connection cannot be established");
            }
        }
    }

    private void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    log.log(Level.FINE, "Failed to close resource", e);
                }
            }
        }
    }

    @Override
    public synchronized boolean initialize() {
        if (initialized) {
            return true;
        } else {
            Connection connection = null;
            PreparedStatement stmt = null;

            try {
                connection = connector.createConnection();
                stmt = connection.prepareStatement(String.format(INITIALIZE_SQL, table));
                stmt.execute();

                this.initialized = true;
                return true;
            } catch (SQLException e) {
                log.log(Level.WARNING, "Failed to initialize SQL health storage", e);
                return false;
            } finally {
                // close resources in reversed order
                closeResources(stmt, connection);
            }
        }
    }

    @Override
    public synchronized boolean terminate() {
        if (!initialized) {
            return true;
        } else {
            offlineData.clear();
            return true;
        }
    }

    @Override
    public synchronized boolean loadPlayer(Player player) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        PreparedStatement createStmt = null;

        HealthData data = new HealthData(backedStorage.getHealth(player), backedStorage.getMaxHealth(player));

        try {
            connection = connector.createConnection();
            stmt = connection.prepareStatement(String.format(PLAYER_LOAD_SQL, table));
            stmt.setString(1, player.getUniqueId().toString());

            result = stmt.executeQuery();
            boolean playerExists = result.next();
            boolean playedBefore = false;

            if (playerExists) {
                respawnStorage.setMaxHealth(player, result.getDouble("MaxHealth"));
                respawnStorage.setHealth(player, result.getDouble("Health"));

                playedBefore = true;
            } else {
                // player not in database; insert data into database
                createStmt = connection.prepareStatement(String.format(PLAYER_CREATE_SQL, table));
                createStmt.setString(1, player.getUniqueId().toString());
                createStmt.setString(2, player.getName());
                createStmt.setDouble(3, data.getHealth());
                createStmt.setDouble(4, data.getMaxHealth());

                if (createStmt.executeUpdate() == 0) {
                    throw new SQLException("Did not insert new player into database");
                }

                playedBefore = false;
            }

            offlineData.put(player, data);

            return playedBefore;
        } catch (SQLException e) {
            log.log(Level.WARNING, String.format("Failed to load player data from database for player %s", player), e);
            return false;
        } finally {
            // close resources in reversed order
            closeResources(createStmt, result, stmt, connection);
        }
    }

    @Override
    public void unloadPlayer(Player player) {
        if (!isPlayerLoaded(player)) {
            return;
        }

        if (!updateDatabase(player)) {
            return;
        }

        HealthData data = offlineData.get(player);
        respawnStorage.setMaxHealth(player, data.getMaxHealth());
        respawnStorage.setHealth(player, data.getHealth());
        offlineData.remove(player);
    }

    private boolean updateDatabase(Player player) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = connector.createConnection();
            stmt = connection.prepareStatement(String.format(PLAYER_UPDATE_SQL, table));
            stmt.setString(1, player.getName());
            stmt.setDouble(2, backedStorage.getHealth(player));
            stmt.setDouble(3, backedStorage.getMaxHealth(player));
            stmt.setString(4, player.getUniqueId().toString());

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Did not update anything");
            }
            return true;
        } catch (SQLException e) {
            log.log(Level.WARNING, String.format("Failed to update player data in database for player %s", player), e);
            return false;
        } finally {
            // close resources in reversed order
            closeResources(stmt, connection);
        }
    }

    @Override
    public boolean isPlayerLoaded(Player player) {
        return offlineData.containsKey(player);
    }

    @Override
    public boolean isRespawn() {
        return backedStorage.isRespawn();
    }

    @Override
    public double getHealth(Player player) {
        return backedStorage.getHealth(player);
    }

    @Override
    public boolean setHealth(Player player, double health) {
        return backedStorage.setHealth(player, health);
    }

    @Override
    public double getMaxHealth(Player player) {
        return backedStorage.getMaxHealth(player);
    }

    @Override
    public boolean setMaxHealth(Player player, double maxHealth) {
        return backedStorage.setMaxHealth(player, maxHealth);
    }

    private static class HealthData {
        private final double health;
        private final double maxHealth;

        private HealthData(double health, double maxHealth) {
            this.health = health;
            this.maxHealth = maxHealth;
        }

        public double getHealth() {
            return health;
        }

        public double getMaxHealth() {
            return maxHealth;
        }
    }
}
