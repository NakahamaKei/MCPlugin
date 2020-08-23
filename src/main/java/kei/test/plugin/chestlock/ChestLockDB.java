package kei.test.plugin.chestlock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import kei.test.plugin.meta.MetaKey;

public class ChestLockDB {
	final private String tableName = "chest";
	private String pathToDB = "jdbc:sqlite:plugins/MyPlugin/ChestLock.db";

	private static ChestLockDB singleton = new ChestLockDB();

	private ChestLockDB() {
	}

	public static ChestLockDB getInstance() {
		return singleton;
	}

	// チェストにロックがかかっているか
	public boolean isLockedOnDB(UUID uuid, Location loc) {
		if (loc == null) {
			System.out.println("存在しない場所が指定されました");
			return false;
		}

		if (Bukkit.getPlayer(uuid) == null) {
			System.out.println("該当プレイヤーは存在しません");
			return false;
		}

		final String SQL = "SELECT * FROM " + tableName + " WHERE uuid=? AND x=? AND y=? AND z=?;";
		try (Connection con = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, uuid.toString());
			ps.setInt(2, loc.getBlockX());
			ps.setInt(3, loc.getBlockY());
			ps.setInt(4, loc.getBlockZ());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return rs.getBoolean("lock");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isLocked(Location loc) {
		if (loc == null) {
			return false;
		}

		Block b = loc.getBlock();
		if (b.hasMetadata(MetaKey.CHEST_LOCK)) {
			return b.getMetadata(MetaKey.CHEST_LOCK).get(0).asBoolean();
		}

		return false;
	}

	public void lockChest(UUID uuid, Location loc) {
		final String SQL = "INSERT INTO " + tableName
				+ " (uuid,name,world,x,y,z,lock) VALUES(?,?,?,?,?,?,?);";

		try (Connection con = DriverManager.getConnection(pathToDB)) {
			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(SQL)) {
				ps.setString(1, uuid.toString());
				ps.setString(2, Bukkit.getPlayer(uuid).getName());
				ps.setString(3, loc.getWorld().getName());
				ps.setInt(4, loc.getBlockX());
				ps.setInt(5, loc.getBlockY());
				ps.setInt(6, loc.getBlockZ());
				ps.setInt(7, 1);

				ps.executeUpdate();
				con.commit();

			} catch (Exception e) {
				con.rollback();
				System.out.println("チェストのロックに失敗しました");
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void unlockChest(UUID uuid, Location loc) {
		final String SQL = "DELETE FROM " + tableName
				+ " WHERE uuid=? AND world=? AND x=? AND y=? AND z=?";

		try (Connection con = DriverManager.getConnection(pathToDB)) {
			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(SQL)) {
				ps.setString(1, uuid.toString());
				ps.setString(2, loc.getWorld().getName());
				ps.setInt(3, loc.getBlockX());
				ps.setInt(4, loc.getBlockY());
				ps.setInt(5, loc.getBlockZ());

				ps.executeUpdate();
				con.commit();
			} catch (Exception e) {
				con.rollback();
				System.out.println("チェストのアンロックに失敗しました");
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UUID getPlayerUUID(Location loc) {
		//		Block b = loc.getBlock();
		//		if (!b.hasMetadata(MetaKey.CHEST_LOCK)) {
		//			return null;
		//		}

		final String SQL = "SELECT * FROM " + tableName + " WHERE x=? AND y=? AND z=?;";
		try (Connection con = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setInt(1, loc.getBlockX());
			ps.setInt(2, loc.getBlockY());
			ps.setInt(3, loc.getBlockZ());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return UUID.fromString(rs.getString("uuid"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getPlayerName(Location loc) {
		//		Block b = loc.getBlock();
		//		if (!b.hasMetadata(MetaKey.CHEST_LOCK)) {
		//			return null;
		//		}

		final String SQL = "SELECT * FROM " + tableName + " WHERE x=? AND y=? AND z=?;";
		try (Connection con = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setInt(1, loc.getBlockX());
			ps.setInt(2, loc.getBlockY());
			ps.setInt(3, loc.getBlockZ());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return rs.getString("name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<ChestLock> getChestLock() {

		final String SQL = "SELECT * FROM " + tableName + ";";
		try (Connection con = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = con.prepareStatement(SQL)) {

			try (ResultSet rs = ps.executeQuery()) {
				ArrayList<ChestLock> chests = new ArrayList<>();
				while (rs.next()) {
					UUID uuid = UUID.fromString(rs.getString("uuid"));
					String name = rs.getString("name");
					World world = Bukkit.getWorld(rs.getString("world"));
					int x = rs.getInt("x");
					int y = rs.getInt("y");
					int z = rs.getInt("z");
					Location loc = new Location(world, x, y, z);
					ChestLock cl = new ChestLock(uuid, name, loc);
					chests.add(cl);
				}
				return chests;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
