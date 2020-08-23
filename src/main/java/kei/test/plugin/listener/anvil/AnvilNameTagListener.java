package kei.test.plugin.listener.anvil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.meta.InstantMessage;
import net.md_5.bungee.api.ChatColor;

public class AnvilNameTagListener implements Listener {

	public AnvilNameTagListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onNameTagOnAnvil(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		if (item == null) {
			return;
		}

		if (item.getType() == Material.AIR) {
			return;
		}

		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return;
		}

		if (event.getInventory().getType() == InventoryType.ANVIL) {
			if (item.getType() == Material.ENCHANTED_BOOK) {
				return;
			}

			String name = meta.getDisplayName();
			if (name.contains(ChatColor.GREEN + "")) {
				event.setCancelled(true);
				//				player.sendMessage(ChatColor.RED + "このアイテムは修理不可能です");
				InstantMessage.showInstantMessage(player, "このアイテムは修理不可能です");
				return;
			}
		}
	}
}
