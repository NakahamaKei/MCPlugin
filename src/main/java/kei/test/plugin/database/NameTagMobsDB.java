package kei.test.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class NameTagMobsDB {
	final private String tableName = "Mob";
	private String pathToDB = "jdbc:sqlite:plugins/MyPlugin/NameTagMobs.db";

	private static NameTagMobsDB singleton = new NameTagMobsDB();

	private NameTagMobsDB() {
	}

	public static NameTagMobsDB getInstance() {
		return singleton;
	}

	public boolean hasNameTag(UUID uuid) {
		final String SQL = "SELECT * FROM " + tableName + " WHERE uuid = ?;";
		try (Connection con = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, uuid.toString());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return rs.getBoolean("hasNameTag");
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return false;
	}

	public void setHasNameTag(UUID uuid) {
		final String SQL = "INSERT INTO " + tableName
				+ " (uuid,hasNameTag) VALUES(?,?);";

		try (Connection con = DriverManager.getConnection(pathToDB)) {
			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(SQL)) {
				ps.setString(1, uuid.toString());
				ps.setBoolean(2, true);

				ps.executeUpdate();
				con.commit();
			} catch (Exception e) {
				con.rollback();
				System.out.println("モブのネームタグ更新に失敗しました");
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<UUID> getNameTagMobs() {
		final String SQL = "SELECT * FROM " + tableName + ";";
		try (Connection con = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = con.prepareStatement(SQL)) {

			try (ResultSet rs = ps.executeQuery()) {
				ArrayList<UUID> uuidList = new ArrayList<>();
				UUID uuid;
				while (rs.next()) {
					uuid = UUID.fromString(rs.getString("uuid"));
					uuidList.add(uuid);
				}
				return uuidList;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}
