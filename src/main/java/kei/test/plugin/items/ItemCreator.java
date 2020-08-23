package kei.test.plugin.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemCreator {

	// 整頓の杖
	public static ItemStack createSortStick() {
		ItemStack item = create(ItemName.SORT_STICK, Material.BLAZE_ROD, 1, ItemLore.SORT_STICK);
		return item;
	}

	public static ItemStack create(String name, Material material, int num, List<String> lores) {
		ItemStack item = new ItemStack(material, num);
		ItemMeta meta = item.getItemMeta();
		if (!meta.getDisplayName().contains(ChatColor.GREEN + "")) {
			meta.setDisplayName(ChatColor.GREEN + name);
		}

		if (lores != null) {
			for (int i = 0; i < lores.size(); i++) {
				lores.set(i, ChatColor.WHITE + lores.get(i));
			}
			meta.setLore(lores);
		}
		item.setItemMeta(meta);

		return item;
	}
}
