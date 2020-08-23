package kei.test.plugin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;

public class PlayerManager {
	private static PlayerManager singleton = new PlayerManager();
	private static HashMap<UUID, PlayerInfo> playerMap = new HashMap<>();

	private PlayerManager() {
	}

	public static PlayerManager getInstance() {
		return singleton;
	}

	public PlayerInfo getPlayerInfo(UUID uuid) {
		return playerMap.get(uuid);
	}

	public HashMap<UUID, PlayerInfo> getPlayerInfos() {
		return playerMap;
	}

	public void registerPlayer(UUID uuid) {
		playerMap.put(uuid, new PlayerInfo(uuid));
	}

	public void unregisterPlayer(UUID uuid) {
		playerMap.remove(uuid);
	}

	public void setPlayerHomeLoc(UUID uuid, Location loc) {
		try {
			PlayerInfo playerInfo = playerMap.get(uuid);
			playerInfo.setHomeLoc(loc);
			playerMap.put(uuid, playerInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// プレイヤー情報が登録されているか
	public boolean exist(UUID uuid) {
		return (playerMap.get(uuid) != null ? true : false);
	}

}
