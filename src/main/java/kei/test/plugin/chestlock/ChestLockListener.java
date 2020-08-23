package kei.test.plugin.chestlock;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.items.ItemName;
import kei.test.plugin.meta.MetaKey;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class ChestLockListener implements Listener {

	private Plugin plugin;

	public ChestLockListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onLockChest(PlayerInteractEvent event) {
		Player clicker = event.getPlayer();
		Block b = event.getClickedBlock();
		Material mat = Material.AIR;
		if (b != null) {
			mat = b.getType();
		}

		Action action = event.getAction();
		Location loc = null;
		if (b != null) {
			loc = b.getLocation();
		}

		ItemStack item = clicker.getInventory().getItemInMainHand();
		String itemName = "";
		if (item != null) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				itemName = meta.getDisplayName();
			}
		}

		if (mat.equals(Material.CHEST)
				|| mat.equals(Material.BARREL)) {

			// 宝箱の鍵を持っている場合
			if (itemName.equals(ItemName.CHEST_KEY)) {

				// ロックが掛かっている場合
				if (b.hasMetadata(MetaKey.CHEST_LOCK)) {
					UUID ownerUUID = ChestLockDB.getInstance().getPlayerUUID(loc);
					Player owner = Bukkit.getPlayer(ownerUUID);

					if ((action.equals(Action.RIGHT_CLICK_BLOCK)
							&& clicker.equals(owner))
							|| (action.equals(Action.RIGHT_CLICK_BLOCK)
									&& (clicker.getGameMode().equals(GameMode.CREATIVE)
											|| clicker.getGameMode().equals(GameMode.SPECTATOR)))) {
						clicker.sendMessage(ChatColor.GREEN + "チェストのロックを解除した");
						ChestLockDB.getInstance().unlockChest(clicker.getUniqueId(), loc);
						b.removeMetadata(MetaKey.CHEST_LOCK, plugin);
						SoundEffect.getInstance().playSoundEffect(clicker, Sound.BLOCK_LEVER_CLICK, 5);
					} else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
						clicker.sendMessage(ChatColor.RED + "このチェストには鍵がかかっている" + ChatColor.GOLD + " [所有者]: "
								+ ChatColor.GREEN + owner.getName());
					}
				} else {
					if (action.equals(Action.RIGHT_CLICK_BLOCK)
							&& !b.hasMetadata("grave")) {
						ChestLockDB.getInstance().lockChest(clicker.getUniqueId(), loc);
						loc.getBlock().setMetadata(MetaKey.CHEST_LOCK, new FixedMetadataValue(plugin, true));
						clicker.sendMessage(
								ChatColor.GREEN + "チェストに鍵をかけた" + ChatColor.GOLD + " [所有者]：" + ChatColor.GREEN
										+ clicker.getName());
						SoundEffect.getInstance().playSoundEffect(clicker, Sound.BLOCK_LEVER_CLICK, 5);
					} else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
						clicker.sendMessage(ChatColor.GOLD + "このチェストには鍵がかかっていないようだ");
					}
				}

				event.setCancelled(true);

			} else {
				// 宝箱の鍵を持っていない場合
				UUID uuid = ChestLockDB.getInstance().getPlayerUUID(loc);
				String name = ChestLockDB.getInstance().getPlayerName(loc);
				if (uuid != null && name != null) {
					//					if (owner == null) {
					//						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
					//						owner = offlinePlayer.getPlayer();
					//					}

					if (!uuid.equals(clicker.getUniqueId())) {
						clicker.sendMessage(
								ChatColor.RED + "このチェストにはロックが掛かっている" + ChatColor.GOLD + " [所有者]: " + ChatColor.GREEN
										+ name);

						SoundEffect.getInstance().playSoundEffect(clicker, Sound.BLOCK_LEVER_CLICK, 5);
						event.setCancelled(true);
					}
				}
			}

			//			else if (b.hasMetadata(MetaKey.CHEST_LOCK)) {
			//				if (b.getMetadata(MetaKey.CHEST_LOCK).get(0).asBoolean()) {
			//					UUID uuid = ChestLockDB.getInstance().getPlayerUUID(b.getLocation());
			//					Player owner = Bukkit.getPlayer(uuid);
			//
			//					if (owner != null && !owner.equals(clicker)) {
			//						clicker.sendMessage(
			//								ChatColor.RED + "このチェストには鍵がかかっているようだ"
			//										+ ChatColor.GOLD + " [所有者]：" + ChatColor.GREEN
			//										+ owner.getName());
			//					} else {
			//						clicker.sendMessage(ChatColor.RED + "このチェストには鍵がかかってるようだ");
			//					}
			//
			//					event.setCancelled(true);
			//				}
			//			}

		}
	}

	@EventHandler
	public void onBreakChest(BlockBreakEvent event) {
		Block b = event.getBlock();
		Material mat = b.getType();

		if (mat.equals(Material.CHEST)
				|| mat.equals(Material.BARREL)) {

			Player breaker = event.getPlayer();
			Location loc = b.getLocation();

			if (b.hasMetadata(MetaKey.CHEST_LOCK)) {
				UUID uuid = ChestLockDB.getInstance().getPlayerUUID(loc);
				String name = ChestLockDB.getInstance().getPlayerName(loc);
				if (breaker.getUniqueId().equals(uuid)) {
					breaker.sendMessage(ChatColor.RED + "不思議な力で守られている。解除するには" + ItemName.CHEST_KEY + "が必要だ");
				} else {
					breaker.sendMessage(ChatColor.RED + "不思議な力でロックされている" + ChatColor.GOLD + " [所有者]: "
							+ ChatColor.GREEN + name);
				}

				event.setCancelled(true);
				return;
			}

			// チェストの鍵持ってたらチェストを壊さない
			ItemStack item = breaker.getInventory().getItemInMainHand();
			if (item != null) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					String name = meta.getDisplayName();
					if (name != null && name.equals(ItemName.CHEST_KEY)) {
						event.setCancelled(true);
						return;
					}
				}
			}

		}
	}

	// TNTの爆発でチェストが爆発しないようにする
	@EventHandler
	public void onDamageChest(EntityExplodeEvent event) {

		if (event.getEntity().getType().equals(EntityType.PRIMED_TNT)
				|| event.getEntity().getType().equals(EntityType.MINECART_TNT)
				|| event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {
			for (Block block : event.blockList().toArray(new Block[event.blockList().size()])) {
				if (block.getType().equals(Material.CHEST)
						|| block.getType().equals(Material.BARREL)) {
					event.blockList().remove(block);
				}
			}
		}
	}

	// ロックされてるチェストの下にホッパーを置けないようにする
	@EventHandler
	public void onPlaceHopperUnderLockChest(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType().equals(Material.HOPPER)) {
			Block upBlock = block.getLocation().add(0.0, 1.0, 0.0).getBlock();
			if (upBlock != null && upBlock.hasMetadata(MetaKey.CHEST_LOCK)) {
				event.setCancelled(true);
			}
		}
	}
}
