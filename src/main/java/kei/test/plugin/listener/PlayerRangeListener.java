package kei.test.plugin.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.Common;
import kei.test.plugin.MobManager;
import kei.test.plugin.MobStatus;
import kei.test.plugin.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerRangeListener implements Listener {

	private double range; // カスタムネームを表示するプレイヤーとエンティティの最短距離
	private long displayNameDuring; // カスタムネームを表示する秒数
	private Plugin plugin;
	private double sqDistance; // プレイヤーと初期リスの距離

	public PlayerRangeListener(Plugin plugin, double range, long displayNameDuring) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.range = range;
		this.displayNameDuring = displayNameDuring;
		this.plugin = plugin;
	}

	public PlayerRangeListener(Plugin plugin) {
		this(plugin, 10L, 5L);
	}

	@EventHandler
	public void onPlayerRange(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		List<Entity> nears = player.getNearbyEntities(range, range, range);

		for (Entity entity : nears) {
			if (entity.getType().equals(EntityType.PLAYER)
					|| entity.getType().equals(EntityType.ENDER_DRAGON)) {
				continue;
			}

			if (entity instanceof LivingEntity) {
				if (entity.getName().contains(ChatColor.AQUA.toString())) {
					continue;
				}

				if (entity.hasMetadata("hasNameTag")) {
					if (entity.getMetadata("hasNameTag").get(0).asBoolean()) {
						continue;
					}
				}

				LivingEntity livingEntity = (LivingEntity) entity;

				UUID uuid = livingEntity.getUniqueId();

				// プレイヤー周辺のモブ情報取得
				MobStatus mobStatus = PlayerManager.getInstance().getPlayerInfo(player.getUniqueId())
						.getMobStatus(uuid);

				// プレイヤーが近くのモブ情報を知らない
				if (mobStatus == null) {
					mobStatus = MobManager.getInstance().getStatus(uuid);
					if (mobStatus == null && livingEntity.isValid()) {
						mobStatus = MobManager.getInstance().createMobStatus(livingEntity);
					}
					// プレイヤーがモブ情報を獲得する
					PlayerManager.getInstance().getPlayerInfo(player.getUniqueId()).addMobStatus(mobStatus);
				}

				if (livingEntity.getType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
					if (player.getGameMode() == GameMode.SURVIVAL
							|| player.getGameMode() == GameMode.ADVENTURE) {
						Monster mons = (Monster) livingEntity;
						mons.setTarget(player);
					}
				}

				// モブの視野範囲にいたら表示名を変更する
				Common.getInstance().setCustomName(livingEntity, mobStatus);
				//				livingEntity.setCustomNameVisible(true);
			}
		}

		// 遠くなったモブデータを削除してネームタグが見えないようにする
		List<Entity> fars = player.getNearbyEntities(range * 2, range * 2, range * 2);
		for (Entity entity : fars) {
			if (!nears.contains(entity)) {
				entity.setCustomNameVisible(false);

				UUID uuid = entity.getUniqueId();
				PlayerManager.getInstance().getPlayerInfo(player.getUniqueId()).removeMobStatus(uuid);
				//				SQLite.getInstance().deleteMobStatus(uuid);

				// 見失わせる
				if (entity instanceof Monster) {
					((Monster) entity).setTarget(null);
				}
			}
		}

		//		sqDistance = player.getWorld().getSpawnLocation().distanceSquared(player.getLocation());
		//		if(sqDistance > 100) {
		//			if(player.hasMetadata("toggledArea", new FixedMetadataValue(plugin, true));
		//		}
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public long getDisplayNameDuring() {
		return displayNameDuring;
	}

	public void setDisplayNameDuring(long displayNameDuring) {
		this.displayNameDuring = displayNameDuring;
	}

}
