package kei.test.plugin.chestlock;

import java.util.UUID;

import org.bukkit.Location;

public class ChestLock {
	private UUID uuid; // 鍵付きチェスト持ち主のUUID
	private String name; // 鍵付きチェスト持ち主の名前
	private Location loc; // 鍵付きチェストの場所

	public ChestLock(UUID uuid, String name, Location loc) {
		this.uuid = uuid;
		this.name = name;
		this.loc = loc;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public Location getLoc() {
		return loc;
	}

}
