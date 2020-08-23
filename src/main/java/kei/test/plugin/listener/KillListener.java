package kei.test.plugin.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import kei.test.plugin.Common;
import kei.test.plugin.MobManager;
import kei.test.plugin.MobStatus;
import kei.test.plugin.PlayerManager;
import kei.test.plugin.SQLite;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class KillListener implements Listener {

	private Map<Projectile, BukkitTask> tasks = new HashMap<>();
	private List<BukkitTask> taskList = new ArrayList<>();

	public KillListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		//		System.out.println(this.getClass().getName() + " is Active");
	}

	@EventHandler
	public void onKillMob(EntityDeathEvent event) {
		Entity deadEntity = event.getEntity();
		Entity killer = event.getEntity().getKiller();

		if (killer instanceof Player) {
			Player player = (Player) killer;
			UUID uuid = player.getUniqueId();

			// 必要ならDB初期化
			if (SQLite.getInstance().existsInKillCountTable(uuid) == false) {
				SQLite.getInstance().initKillCount(uuid);
			}

			int count = 0;
			if (deadEntity instanceof Zombie) {
				count = SQLite.getInstance().getKillCount(uuid, "zombie") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "zombie", count);
			} else if (deadEntity instanceof Skeleton) {
				count = SQLite.getInstance().getKillCount(uuid, "skeleton") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "skeleton", count);
			} else if (deadEntity instanceof Creeper) {
				count = SQLite.getInstance().getKillCount(uuid, "creeper") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "creeper", count);
			} else if (deadEntity instanceof Spider) {
				count = SQLite.getInstance().getKillCount(uuid, "spider") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "spider", count);
			} else if (deadEntity instanceof Slime) {
				count = SQLite.getInstance().getKillCount(uuid, "slime") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "slime", count);
			} else if (deadEntity instanceof Enderman) {
				count = SQLite.getInstance().getKillCount(uuid, "enderman") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "enderman", count);
			} else if (deadEntity instanceof Blaze) {
				count = SQLite.getInstance().getKillCount(uuid, "blaze") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "blaze", count);
			} else if (deadEntity instanceof Ghast) {
				count = SQLite.getInstance().getKillCount(uuid, "ghast") + 1;
				SQLite.getInstance().updateKillCount(player.getUniqueId(), "ghast", count);
			}

			//			new BukkitRunnable() {
			//				double t = 0;
			//				Location loc = player.getLocation();
			//				Vector dir = loc.getDirection().normalize();
			//
			//				public void run() {
			//					t = t + 0.5;
			//					double x = dir.getX() * t;
			//					double y = dir.getY() * t + 1.5;
			//					double z = dir.getZ() * t;
			//
			//					loc.add(x, y, z);
			//					player.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0, 1);
			//					loc.subtract(x, y, z);
			//					if (t > 30) {
			//						this.cancel();
			//					}
			//				}
			//			}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0, 1);
		}

		// 死んだモブをマップから消す
		UUID uuid = event.getEntity().getUniqueId();
		MobManager.getInstance().remove(uuid);

		//		SQLite.getInstance().existsInKillCountTable(uuid);
		//		SQLite.getInstance().deleteMobStatus(uuid);
	}

	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof LivingEntity) {

			LivingEntity livingEntity = (LivingEntity) entity;
			if (entity.isDead()) {
				return;
			}

			if (event.getDamager() instanceof Player) {
				Player damager = ((Player) event.getDamager());
				ItemStack weapon = damager.getEquipment().getItemInMainHand();
				if (weapon != null) {
					ItemMeta meta = weapon.getItemMeta();
					if (meta != null && weapon.getItemMeta().getDisplayName()
							.equals(ChatColor.GREEN + "渾身の一撃")) {
						((LivingEntity) event.getDamager()).damage(6.0);
					}
				}
			}

			if (entity instanceof Player)
				return;

			String customName = "";

			Entity damager1 = event.getDamager();
			if (damager1 instanceof Player) {
				Player player = (Player) damager1;
				MobStatus mobStatus = PlayerManager.getInstance().getPlayerInfo(player.getUniqueId())
						.getMobStatus(entity.getUniqueId());
				if (mobStatus != null
						&& !livingEntity.hasMetadata("hasNameTag")) {
					//				int hp = (int) (Math.max(0, livingEntity.getHealth() - event.getDamage()));
					Common.getInstance().setCustomName(livingEntity, mobStatus);
					customName = livingEntity.getCustomName();
				}

				if (livingEntity instanceof Monster) {
					Monster mons = (Monster) livingEntity;
					mons.setTarget(player);
				}
			}

			// アクションバーに敵体力を表示する
			if (damager1 instanceof Player) {
				Player player = (Player) damager1;

				List<String> msg = Arrays.asList(customName);

				taskList.add(new BukkitRunnable() {
					private int count = 0;

					@Override
					public void run() {
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg.get(0)));

						if (taskList.size() > 1) {
							taskList.get(0).cancel();
							taskList.remove(0);
						}
						count++;

						if (count >= 5) {
							this.cancel();
						}
					}
				}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0L, 20L));
			}
		}
	}

	// 弓を撃った時
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		//		if (e.getEntity().getShooter() instanceof Player) {
		//			Projectile entity = e.getEntity();
		//
		//			if (entity.getType() == EntityType.ARROW) {
		//				World world = entity.getWorld();
		//				tasks.put(e.getEntity(), new BukkitRunnable() {
		//					@Override
		//					public void run() {
		//						Location loc = entity.getLocation();
		//						world.spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0, 1);
		//					}
		//				}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0, 1));
		//			}
		//		}
	}

	// 弓が当たった時
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		//		if (e.getEntity().getShooter() instanceof Player) {
		//			BukkitTask task = tasks.get(e.getEntity());
		//			if (task != null) {
		//				task.cancel();
		//				tasks.remove(e.getEntity());
		//			}
		//		}

		if ((e.getEntity().getShooter() instanceof Stray)
				&& (e.getHitEntity() != null)) {
			Stray stray = (Stray) e.getEntity().getShooter();

			stray.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));

			if (stray.getEntitySpawnReason().equals(SpawnReason.CUSTOM)
					|| stray.getEntitySpawnReason().equals(SpawnReason.NATURAL)
					|| stray.getEntitySpawnReason().equals(SpawnReason.SPAWNER_EGG)) {
				Location loc = stray.getLocation();

				Entity entity = e.getHitEntity();
				if (entity != null && entity instanceof Player) {
					Player player = (Player) entity;
					player.teleport(loc);
					SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5);
					//					livEntity.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 20 * 300, 5));
				}

			}
		}
	}

}
