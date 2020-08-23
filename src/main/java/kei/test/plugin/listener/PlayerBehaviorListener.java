package kei.test.plugin.listener;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.PlayerInfo;
import kei.test.plugin.PlayerManager;
import kei.test.plugin.WarpPortal;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class PlayerBehaviorListener implements Listener {
	Plugin plugin;

	public PlayerBehaviorListener(Plugin p) {
		p.getServer().getPluginManager().registerEvents(this, p);
		this.plugin = p;
	}

	@EventHandler
	public void onSneaking(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();

		// スニークしたらポータルでワープする
		if (player.isSneaking()) {
			Location foot = player.getLocation().add(0D, -1D, 0D).getBlock().getLocation();

			Block footBlock = foot.getBlock();
			if (footBlock == null)
				return;

			if (footBlock.hasMetadata("portal") && footBlock.hasMetadata("placer")) {
				//				UUID uuid = player.getUniqueId();
				Player placer = ((Player) footBlock.getMetadata("placer").get(0).value());
				UUID uuid = placer.getUniqueId();
				PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);
				Location first = null;
				Location second = null;
				if (playerInfo.getPortal().getPortalNum() == 2) {
					first = playerInfo.getPortal().getFirstLocation();
					second = playerInfo.getPortal().getSecondLocation();
				}

				double fx = 0, fy = 0, fz = 0;
				if (first != null) {
					fx = first.getBlockX();
					fy = first.getBlockY();
					fz = first.getBlockZ();
				}

				double sx = 0, sy = 0, sz = 0;
				if (second != null) {
					sx = second.getBlockX();
					sy = second.getBlockY();
					sz = second.getBlockZ();
				}

				double footX = foot.getBlockX();
				double footY = foot.getBlockY();
				double footZ = foot.getBlockZ();

				if (first != null && second != null) {
					if (footX == fx && footY == fy && footZ == fz) {
						second.add(0D, 1D, 0D);
						player.teleport(second);
						second.add(0D, -1D, 0D);
						SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5.0);
						//						player.playSound(first, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
						//						player.playSound(second, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
					} else if (footX == sx && footY == sy && footZ == sz) {
						first.add(0D, 1D, 0D);
						player.teleport(first);
						first.add(0D, -1D, 0D);
						SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5.0);
						//						player.playSound(first, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
						//						player.playSound(second, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
					} else {
						player.sendMessage("テレポートに失敗しました");
					}
				}
			}

		}
	}

	@EventHandler
	public void onPlacePortal(BlockPlaceEvent event) {
		ItemStack is = event.getItemInHand();
		if (!is.hasItemMeta())
			return;

		ItemMeta im = is.getItemMeta();
		String name = im.getDisplayName();

		if (name.equals(ChatColor.GREEN + "ポータル")) {
			UUID uuid = event.getPlayer().getUniqueId();
			PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);

			// まだポータルがない場合は生成する
			if (playerInfo.getPortal().getPortalNum() < 1) {
				playerInfo.setPortal(new WarpPortal());
			}

			// ポータルブロックにワープ地点を指定
			Location loc = event.getBlock().getLocation();
			PlayerManager.getInstance().getPlayerInfo(uuid).getPortal().addPortalLocation(loc);

			if (!event.getBlock().hasMetadata("portal")) {
				event.getBlock().setMetadata("portal", new FixedMetadataValue(plugin, event.getBlock()));
				event.getBlock().setMetadata("placer", new FixedMetadataValue(plugin, event.getPlayer()));
			}

			event.getPlayer().getInventory().remove(is);
			event.getPlayer().getInventory().addItem(is);
		}
	}

	@EventHandler
	public void onInteractPortal(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		// ポータル右クリックで破壊する
		Block block = event.getClickedBlock();
		if (block.hasMetadata("portal")) {
			UUID uuid = event.getPlayer().getUniqueId();
			WarpPortal portal = PlayerManager.getInstance().getPlayerInfo(uuid).getPortal();
			if (portal != null) {
				portal.breakPortal();
			}
		}

	}
}
