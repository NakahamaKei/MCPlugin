package kei.test.plugin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;

public class WarpPortal {
	private ArrayList<Location> locs = new ArrayList<>();

	public WarpPortal() {
	}

	public void addPortalLocation(Location loc) {
		if (locs.size() >= 2) {
			locs.get(0).getBlock().setType(Material.AIR);
			locs.remove(0);
		}
		locs.add(loc);
	}

	public int getPortalNum() {
		return locs.size();
	}

	public Location getFirstLocation() {
		if (locs.size() >= 1) {
			return locs.get(0);
		}
		return null;
	}

	public Location getSecondLocation() {
		if (locs.size() >= 2) {
			return locs.get(1);
		}
		return null;
	}

	public void breakPortal() {
		for(Location loc : locs) {
			loc.getBlock().setType(Material.AIR);
		}
	}
}
