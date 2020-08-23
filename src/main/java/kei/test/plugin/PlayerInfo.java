package kei.test.plugin;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;

public class PlayerInfo {
	private SQLite sqlite = SQLite.getInstance();
	private UUID uuid;
	private Location homeLoc; // worldのホーム位置
	//	private HashMap<UUID, MobStatus> statusMap = new HashMap<>(); // 近くのモブ情報
	private ArrayList<MobStatus> statusList = new ArrayList<>(); // 近くのモブ情報
	private WarpPortal portal = new WarpPortal();

	public PlayerInfo(UUID uuid) {
		this.uuid = uuid;
	}

	public int getKillCount(String type) {
		return sqlite.getKillCount(uuid, type);
	}

	public String getKillCountString(String type) {
		return String.valueOf(sqlite.getKillCount(uuid, type));
	}

	public Location getHomeLoc() {
		return homeLoc;
	}

	public void setHomeLoc(Location homeLoc) {
		this.homeLoc = homeLoc;
	}

	// プレイヤー近くのモブ情報を登録
	public void addMobStatus(MobStatus status) {
		if (status == null) {
			return;
		}
		//		this.statusMap.put(status.getUuid(), status);
		// 30件以上登録されてたら先頭のやつを忘れる
		if (statusList.size() >= 1000) {
			statusList.remove(0);
		}
		this.statusList.add(status);
	}

	// プレイヤー近くのモブ情報取得
	public MobStatus getMobStatus(UUID uuid) {
		//		MobStatus mobStatus = this.statusMap.get(uuid);
		MobStatus mobStatus = null;
		for (MobStatus status : statusList) {
			if (status != null && uuid.equals(status.getUuid())) {
				mobStatus = status;
				break;
			}
		}
		return mobStatus;
	}

	// プレイヤーがモブ情報を忘れる
	public void removeMobStatus(UUID uuid) {
		for (int i = 0; i < statusList.size(); i++) {
			if (statusList.get(i) == null)
				continue;

			if (statusList.get(i).getUuid().equals(uuid)) {
				statusList.remove(i);
			}
		}
	}

	// 登録されているモブの数を返す
	public int getMobListSize() {
		return statusList.size();
	}

	public WarpPortal getPortal() {
		return portal;
	}

	public void setPortal(WarpPortal portal) {
		this.portal = portal;
	}

}
