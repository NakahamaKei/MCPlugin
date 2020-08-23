package kei.test.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLite {
	final private String killCountTableName = "KillCount";
	final private String mobStatusTableName = "MobStatus";
	//	private String pathToDB = "jdbc:sqlite:/D:/minecraft/Servers/plugins/test.db";
	//	private String pathToDB = "jdbc:sqlite:/root/home/minecraft/PaperMC/plugins/test.db";
	private String pathToDB = "jdbc:sqlite:plugins/test.db";

	private static SQLite singleton = new SQLite();

	public static SQLite getInstance() {
		return singleton;
	}

	private SQLite() {
	}

	public boolean existsInKillCountTable(UUID uuid) {
		final String SQL = "SELECT * FROM " + killCountTableName + " WHERE playerID = ?;";
		try (Connection connection = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setString(1, uuid.toString());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public int getKillCount(UUID uuid, String type) {
		final String SQL = "SELECT * FROM " + killCountTableName + " WHERE playerID = ?;";
		try (Connection connection = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setString(1, uuid.toString());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return rs.getInt(type);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public void initKillCount(UUID playerUUID) {
		final String SQL = "INSERT INTO " + killCountTableName + " (playerID) VALUES(?);";
		try (Connection connection = DriverManager.getConnection(pathToDB)) {
			connection.setAutoCommit(false);

			try (PreparedStatement ps = connection.prepareStatement(SQL)) {
				ps.setString(1, playerUUID.toString());

				ps.executeUpdate();
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
				System.out.println("キルカウント挿入に失敗しました");
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateKillCount(UUID playerUUID, String type, int killNum) {
		final String SQL = "UPDATE " + killCountTableName + " SET " + type + " = ? WHERE playerID = ?;";
		try (Connection connection = DriverManager.getConnection(pathToDB)) {
			connection.setAutoCommit(false);

			//			// モブの討伐数を記録
			//			sql = "UPDATE KillCount SET " + type + " = " + killNum + " WHERE playerID = '" + uuid + "'";
			//			statement.executeUpdate(sql);
			try (PreparedStatement ps = connection.prepareStatement(SQL)) {
				//				ps.setString(1, type);
				ps.setInt(1, killNum);
				ps.setString(2, playerUUID.toString());
				ps.executeUpdate();
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
				System.out.println("キルカウント更新をロールバックしました");
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// モブの全情報を書き込む
	public void writeMobInfo(UUID uuid, String name, int level, double maxHp, double hp, double attack) {
		final String SQL = "INSERT INTO " + mobStatusTableName
				+ " (uuid,name,level,maxhp,hp,attack) VALUES(?,?,?,?,?,?);";

		try (Connection connection = DriverManager.getConnection(pathToDB)) {
			connection.setAutoCommit(false);

			try (PreparedStatement ps = connection.prepareStatement(SQL)) {
				ps.setString(1, uuid.toString());
				ps.setString(2, name);
				ps.setInt(3, level);
				ps.setDouble(4, maxHp);
				ps.setDouble(5, hp);
				ps.setDouble(6, attack);

				ps.executeUpdate();
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
				System.out.println("ロールバックしました");
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// モブ情報を取得
	public MobStatus getMobStatus(UUID uuid) {
		final String SQL = "SELECT * FROM " + mobStatusTableName + " WHERE uuid = ?";
		try (Connection connection = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setString(1, uuid.toString());

			try (ResultSet rs = ps.executeQuery()) {
				// モブ情報を組み立てる
				while (rs.next()) {
					String name = rs.getString("name");
					int level = rs.getInt("level");
					double maxHp = rs.getDouble("maxhp");
					double hp = rs.getDouble("hp");
					double attack = rs.getDouble("attack");

					//					MobStatus mobStatus = new MobStatus(uuid, name, level, maxHp, hp, attack);
					//					MobStatus mobStatus = new MobStatus(uuid, name, level, maxHp, hp, attack);
					//					return mobStatus;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void deleteMobStatus(UUID uuid) {

		final String SQL = "DELETE FROM " + mobStatusTableName + " WHERE uuid = ?;";
		try (Connection connection = DriverManager.getConnection(pathToDB)) {

			connection.setAutoCommit(false);

			try (PreparedStatement ps = connection.prepareStatement(SQL)) {
				ps.setString(1, uuid.toString());
				ps.executeUpdate();
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
				System.out.println("モブ情報削除をロールバックしました");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean existsInMobStatusTable(UUID uuid) {
		final String SQL = "SELECT * FROM " + killCountTableName + " WHERE uuid = ?;";
		try (Connection connection = DriverManager.getConnection(pathToDB);
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setString(1, uuid.toString());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
