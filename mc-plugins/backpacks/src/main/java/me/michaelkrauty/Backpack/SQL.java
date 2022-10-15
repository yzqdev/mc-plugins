package me.michaelkrauty.Backpack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created on 8/2/2014.
 *
 * @author michaelkrauty
 */
public class SQL {

	private static Main main;

	private static Connection connection;

	private final String jdbc;
	private final String user;
	private final String pass;
	private final String table;

	public SQL(Main main) {
		jdbc = "jdbc:mysql://" + Main.config.getString("mysql.host") + "/" + Main.config.getString("mysql.database");
		user = Main.config.getString("mysql.user");
		pass = Main.config.getString("mysql.pass");
		table = Main.config.getString("mysql.table");
		SQL.main = main;
		checkTables();
	}

	public synchronized void openConnection() {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(jdbc, user, pass);
			}
			if (connection.isClosed()) {
				connection = DriverManager.getConnection(jdbc, user, pass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void closeConnection() {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean checkTables() {
		openConnection();
		boolean res = true;
		try {
			PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "` (uuid varchar(256) PRIMARY KEY, inventory longtext);");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			res = false;
		}
		return res;
	}

	public synchronized boolean backpackExists(String uuid) {
		try {
			openConnection();
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid`=?;");
			sql.setString(1, uuid);
			ResultSet result = sql.executeQuery();
			return result.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized void addBackpack(String uuid) {
		try {
			openConnection();
			PreparedStatement sql = connection
					.prepareStatement("INSERT INTO `" + table + "`(`uuid`, `inventory`) VALUES (?,?)");
			sql.setString(1, uuid);
			sql.setString(2, Main.toBase64(main.getServer().createInventory(null, 54)));
			sql.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized String getBackpackInventory(String uuid) {
		try {
			openConnection();
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid`=?;");
			sql.setString(1, uuid);
			ResultSet result = sql.executeQuery();
			result.next();
			return result.getString("inventory");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void loadAllBackpacks() {
		try {
			openConnection();
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM `" + table + "`;");
			ResultSet result = sql.executeQuery();
			result.last();
			int items = result.getRow();
			result.first();
			for (int i = 0; i < items; i++) {
				if (main.getBackpack(result.getString("uuid")) == null) {
					main.backpacks.add(new Backpack(main, result.getString("uuid")));
				}
				result.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void updateBackpack(String uuid, String inventory) {
		try {
			openConnection();
			PreparedStatement sql = connection
					.prepareStatement("UPDATE `" + table + "` SET `inventory`=? WHERE `uuid`=?;");
			sql.setString(1, inventory);
			sql.setString(2, uuid);
			sql.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeBackpack(String uuid) {
		try {
			openConnection();
			PreparedStatement sql = connection
					.prepareStatement("DELETE FROM `" + table + "` WHERE `uuid`=?;");
			sql.setString(1, uuid);
			sql.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
