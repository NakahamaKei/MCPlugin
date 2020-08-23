package kei.test.plugin.dangeonmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class DangeonMobManager {

	private Map<Integer, List<LivingEntity>> mobs = new HashMap<>();
	private List<Location> spawnLocations = new ArrayList<>();
	private int spawnLimit;

	public DangeonMobManager() {
	}

	// モブグループの数を取得する
	public int getMobCount(int groupIndex) {
		if (mobs.get(groupIndex) != null) {
			return mobs.get(groupIndex).size();
		}
		return 0;
	}

	// 現在のモブグループの数を取得する
	public int getMobGroupCount() {
		return mobs.size();
	}

	public void addMobIntoGroup(int groupIndex, LivingEntity entity) {
		if (mobs.get(groupIndex) == null) {
			mobs.put(0, new ArrayList<>());
		}

		if (mobs.get(groupIndex).size() < spawnLimit) {
			mobs.get(groupIndex).add(entity);
		}
	}

	public LivingEntity getMob(int groupIndex, int indivisualIndex) {
		return mobs.get(groupIndex).get(indivisualIndex);
	}

	public void removeMobFromGroup(int groupIndex, int indivisualIndex) {
		mobs.get(groupIndex).remove(indivisualIndex);
	}

	public LivingEntity popMobFromGroup(int groupIndex) {
		return mobs.get(groupIndex).remove(0);
	}

	public void addSpawnLocation(Location location) {
		//		int x = location.getBlockX();
		//		int y = location.getBlockY();
		//		int z = location.getBlockZ();
		//
		//		for (int i = 0; i < spawnLocations.size(); i++) {
		//			int tx = spawnLocations.get(i).getBlockX();
		//			int ty = spawnLocations.get(i).getBlockY();
		//			int tz = spawnLocations.get(i).getBlockZ();
		//
		//			if (x != tx && y != ty && z != tz) {
		//				spawnLocations.add(location);
		//				break;
		//			}
		//		}
		spawnLocations.add(location);
	}

	public void setSpawnLocation(int index, Location location) {
		spawnLocations.set(index, location);
	}

	public Location getSpawnLocation(int index) {
		return spawnLocations.get(index);
	}

	public int getSpawnLimit() {
		return spawnLimit;
	}

	public void setSpawnLimit(int spawnLimit) {
		this.spawnLimit = spawnLimit;
	}
}
