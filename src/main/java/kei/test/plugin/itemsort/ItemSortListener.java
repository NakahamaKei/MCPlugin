package kei.test.plugin.itemsort;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.items.ItemName;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class ItemSortListener implements Listener {

	public ItemSortListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onItemSort(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item != null
				&& action.equals(Action.LEFT_CLICK_BLOCK)
				&& block != null
				&& block.getType().equals(Material.CHEST)) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null && meta.getDisplayName().equals(ItemName.SORT_STICK)) {
				player.sendMessage(ChatColor.GREEN + "中身が整頓された");

				SoundEffect.getInstance().playSoundEffect(player, Sound.BLOCK_LEVER_CLICK, 5);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreakChestWithSortStick(BlockBreakEvent event) {
		Material material = event.getBlock().getType();
		Player breaker = event.getPlayer();
		ItemStack item = breaker.getInventory().getItemInMainHand();

		if (material.equals(Material.CHEST)
				&& item != null) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null && meta.getDisplayName().equals(ItemName.SORT_STICK)) {
				breaker.sendMessage(ChatColor.RED + "整頓の杖を持った状態ではチェストを破壊できません");
				event.setCancelled(true);
			}
		}
	}

	private void sortInventory(Location loc) {
		Block block = loc.getBlock();
		if (block == null) {
			return;
		}

		if (!block.getType().equals(Material.CHEST)) {
			return;
		}

		Chest chest = (Chest) block.getState();

		Inventory inv = chest.getInventory();
		List<ItemStack> items = Arrays.asList(inv.getContents());

		inv.clear();

	}

}
