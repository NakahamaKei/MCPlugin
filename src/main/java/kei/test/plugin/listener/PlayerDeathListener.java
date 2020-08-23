package kei.test.plugin.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class PlayerDeathListener implements Listener {

	private Plugin plugin;
	private final String metaDataKey = "grave";

	public PlayerDeathListener(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onOpenGrave(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Block b = event.getClickedBlock();
		Player player = event.getPlayer();
		if (b.hasMetadata(metaDataKey)) {
			String name = b.getMetadata(metaDataKey).get(0).asString();
			if (name.equals(player.getName())) {
				return;
			} else if (player.getGameMode().equals(GameMode.CREATIVE)) {
				player.sendMessage(ChatColor.RED + "このお墓は" + ChatColor.GREEN + name
						+ ChatColor.RED + "さんのものです");
				return;
			} else {
				player.sendMessage(ChatColor.RED + "このお墓は" + ChatColor.GREEN + name
						+ ChatColor.RED + "さんのものです");
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onBreakGrave(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block b = event.getBlock();

		if (b.hasMetadata(metaDataKey)) {
			String name = b.getMetadata(metaDataKey).get(0).asString();
			if (name.equals(player.getName())
					|| player.getGameMode().equals(GameMode.CREATIVE)) {
				b.removeMetadata(metaDataKey, plugin);
				return;
			} else {
				player.sendMessage(ChatColor.RED + "このお墓は" + ChatColor.GREEN + name
						+ ChatColor.RED + "さんのものです");
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getDrops().isEmpty()) {
			return;
		}

		Player player = event.getEntity();
		Location graveLoc = player.getLocation();

		Location deathLoc = player.getLocation();
		player.sendMessage(
				ChatColor.GREEN + "(x, y, z) = (" + (int) deathLoc.getX() + ", " + (int) deathLoc.getY() + ", "
						+ (int) deathLoc.getZ() + ") にお墓が立ちました");

		int limit = 0;
		while (graveLoc.getBlock().getType() != Material.AIR) {
			graveLoc.add(0D, 1.0D, 0D);
			limit++;
			if (limit >= 255) {
				player.sendMessage("墓を正常に作成できませんでした");
				return;
			}
		}

		player.setExp(player.getExp());

		graveLoc.getBlock().setType(Material.CHEST);
		Block block = graveLoc.getBlock();

		if (block.getType() == Material.CHEST) {
			Chest chest = (Chest) block.getState();
			Inventory inv = chest.getInventory();
			chest.setMetadata(metaDataKey, new FixedMetadataValue(plugin, player.getName()));

			List<ItemStack> items = new ArrayList<>();
			for (ItemStack item : event.getDrops()) {
				items.add(item);
			}

			for (int i = 0; i < Math.min(27, items.size()); i++) {
				//				inv.addItem(items.get(i));
				ItemStack item = items.get(i);
				inv.setItem(i, item);
			}

			if (items.size() > 27) {
				limit = 0;
				while (graveLoc.getBlock().getType() != Material.AIR) {
					graveLoc.add(0D, 1.0D, 0D);
					limit++;
					if (limit >= 255) {
						player.sendMessage("墓を正常に作成できませんでした");
						return;
					}
				}

				graveLoc.getBlock().setType(Material.CHEST);
				block = graveLoc.getBlock();
				if (block.getType() == Material.CHEST) {
					chest = (Chest) block.getState();
					chest.setMetadata(metaDataKey, new FixedMetadataValue(plugin, player.getName()));

					inv = chest.getInventory();

					for (int i = 27; i < items.size(); i++) {
						ItemStack item = items.get(i);
						inv.setItem(i - 27, item);
					}
				}
			}
		}

		event.getDrops().clear();
	}

	@EventHandler
	public void onPlayerKillByPlayer(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();

		if(entity instanceof Player) {
			Player killer = entity.getKiller();

			if(killer != null) {
				killer.kickPlayer("プレイヤーを殺害したため、キックされました。一定回数を超えるとBANになります。");
			}
		}
	}

}
