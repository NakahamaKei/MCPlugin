package kei.test.plugin;

import java.util.HashSet;

import org.bukkit.Material;

public class Bed {
	private static Bed singleton = new Bed();
	private HashSet<Material> beds = new HashSet<>();

	private Bed() {
		beds.add(Material.BLACK_BED);
		beds.add(Material.BLUE_BED);
		beds.add(Material.BROWN_BED);
		beds.add(Material.CYAN_BED);
		beds.add(Material.GRAY_BED);
		beds.add(Material.GREEN_BED);
		beds.add(Material.LIGHT_BLUE_BED);
		beds.add(Material.LIGHT_GRAY_BED);
		beds.add(Material.LIME_BED);
		beds.add(Material.MAGENTA_BED);
		beds.add(Material.ORANGE_BED);
		beds.add(Material.PINK_BED);
		beds.add(Material.PURPLE_BED);
		beds.add(Material.RED_BED);
		beds.add(Material.WHITE_BED);
		beds.add(Material.YELLOW_BED);
	}

	public static Bed getInstance() {
		return singleton;
	}

	public HashSet<Material> getBeds(){
		return beds;
	}

	// 引数はベッドか？
	public boolean isBed(Material material) {
		return beds.contains(material);
	}
}
