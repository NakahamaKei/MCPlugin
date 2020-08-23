package kei.test.plugin.listener.mobspawn;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.MobManager;

public class MobSpawnListener implements Listener {

	private Plugin plugin;
	LivingEntity entity;

	public MobSpawnListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		World world = event.getEntity().getWorld();
		if (MobManager.getInstance().getMobAmount() >= 2500
				&& !world.getName().contains("dangeon")) {
			event.setCancelled(true);
			return;
		}

		// プレイヤーから遠いモブは沸かせない
		if (!canSpawn(event)) {
			event.setCancelled(true);
			return;
		}

		entity = event.getEntity();
		if (!(entity instanceof Monster)) {
			return;
		}

		// モブステータス作成して登録
		MobManager.getInstance().createMobStatus(entity);
		event.getEntity().setCanPickupItems(false);

		// 装備ドロップを無効にする
		this.setEquipmentDropChanceZero();

		// 自然スポーンじゃない
		if (event.getSpawnReason() == SpawnReason.NATURAL
				|| event.getSpawnReason() == SpawnReason.SPAWNER_EGG) {
		} else {
			return;
		}

		// ゾンビにヘルメットをかぶせる
		if (entity.getType().equals(EntityType.ZOMBIE)) {
			if (entity.getType().equals(EntityType.DROWNED)) {
				event.setCancelled(true);
			}

			Zombie zombie = (Zombie) entity;
			ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
			zombie.getEquipment().setHelmet(helm);
			zombie.getEquipment().setHelmetDropChance(0.0f);

			int rand = (int) (Math.random() * 3) + 1;
			Location curLoc = zombie.getLocation();
			for (int i = 0; i < rand; i++) {
				int type = (int) (Math.random() * 4) + 1;
				switch (type) {
				case 1:
					zombie.getWorld().spawnEntity(curLoc, EntityType.HUSK, SpawnReason.CUSTOM);
					break;
				case 2:
					zombie.getWorld().spawnEntity(curLoc, EntityType.ZOMBIFIED_PIGLIN, SpawnReason.CUSTOM);
					break;
				case 3:
					zombie.getWorld().spawnEntity(curLoc, EntityType.STRAY, SpawnReason.CUSTOM);
					break;
				case 4:
					zombie.getWorld().spawnEntity(curLoc, EntityType.VINDICATOR, SpawnReason.CUSTOM);
					break;
				case 5:
					zombie.getWorld().spawnEntity(curLoc, EntityType.PILLAGER, SpawnReason.CUSTOM);
					break;
				default:
					zombie.getWorld().spawnEntity(curLoc, EntityType.HUSK, SpawnReason.CUSTOM);
					break;
				}
			}

			return;
		}

		if (entity.getType().equals(EntityType.CREEPER) || entity.getType().equals(EntityType.SKELETON)) {

			int rand = (int) (Math.random() * 3) + 1;
			Location curLoc = entity.getLocation();
			for (int i = 0; i < rand; i++) {
				int type = (int) (Math.random() * 5) + 1;
				switch (type) {
				case 1:
					entity.getWorld().spawnEntity(curLoc, EntityType.HUSK, SpawnReason.CUSTOM);
					break;
				case 2:
					entity.getWorld().spawnEntity(curLoc, EntityType.ZOMBIFIED_PIGLIN, SpawnReason.CUSTOM);
					break;
				case 3:
					entity.getWorld().spawnEntity(curLoc, EntityType.STRAY, SpawnReason.CUSTOM);
					break;
				case 4:
					entity.getWorld().spawnEntity(curLoc, EntityType.VINDICATOR, SpawnReason.CUSTOM);
					break;
				case 5:
					entity.getWorld().spawnEntity(curLoc, EntityType.ZOMBIE, SpawnReason.CUSTOM);
					break;
				default:
					entity.getWorld().spawnEntity(curLoc, EntityType.HUSK, SpawnReason.CUSTOM);
					break;
				}
			}

			return;
		}

		if (entity.getType().equals(EntityType.PILLAGER)) {

			Pillager pillager = (Pillager) entity;
			pillager.setPatrolLeader(false);
		}

		if (entity.getType().equals(EntityType.STRAY)) {
			Stray stray = (Stray) entity;
			stray.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			stray.getEquipment().setHelmetDropChance(0.0f);
			stray.getEquipment().setItemInMainHandDropChance(0.0f);
		}

	}

	private boolean canSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (entity != null && entity.getType().equals(EntityType.ENDER_DRAGON)) {
			return true;
		}

		// プレイヤーから遠いとスポーンさせない
		List<Player> players = event.getEntity().getWorld().getPlayers();
		if (players != null && !players.isEmpty()) {
			int distance = 100000;
			boolean check = false;
			for (Player p : players) {
				distance = (int) p.getLocation().distanceSquared(event.getEntity().getLocation());
				//				System.out.println(distance);
				int height = Math.abs(p.getLocation().getBlockY() - event.getEntity().getLocation().getBlockY());
				if (distance <= 2500 && height <= 35) {
					check = true;
					break;
				}
			}

			if (!check) {
				return false;
			}
		}

		// モブが密集しすぎなら沸かせない
		//		List<Entity> entities = event.getEntity().getNearbyEntities(5.0, 5.0, 5.0);
		//		int count = 0;
		//		for (Entity e : entities) {
		//			if (e instanceof Monster) {
		//				count++;
		//			}
		//			if (count >= 3) {
		//				return false;
		//			}
		//		}

		return true;
	}

	private void setEquipmentDropChanceZero() {
		entity.getEquipment().setHelmetDropChance(0.0f);
		entity.getEquipment().setChestplateDropChance(0.0f);
		entity.getEquipment().setLeggingsDropChance(0.0f);
		entity.getEquipment().setBootsDropChance(0.0f);
		entity.getEquipment().setItemInMainHandDropChance(0.0f);
	}

	private ItemStack getRandomHelmet(int level) {
		ItemStack item = null;

		if (level >= 15) {
			int type = (int) (Math.random() * 2);
			if (type == 0) {
				item = new ItemStack(Material.DIAMOND_HELMET);
			} else {
				item = new ItemStack(Material.IRON_HELMET);
			}
		} else if (level >= 10) {
			int type = (int) (Math.random() * 2);
			if (type == 0) {
				item = new ItemStack(Material.IRON_HELMET);
			} else {
				item = new ItemStack(Material.GOLDEN_HELMET);
			}
		} else {
			int type = (int) (Math.random() * 2);
			if (type == 0) {
				item = new ItemStack(Material.GOLDEN_HELMET);
			} else {
				item = new ItemStack(Material.LEATHER_HELMET);
			}
		}

		return item;
	}

	private ItemStack getRandomChestPlate(int level) {
		ItemStack item = null;

		if (level >= 15) {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.DIAMOND_CHESTPLATE);
			} else if (type == 1) {
				item = new ItemStack(Material.IRON_CHESTPLATE);
			}
		} else if (level >= 10) {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.IRON_CHESTPLATE);
			} else if (type == 1) {
				item = new ItemStack(Material.GOLDEN_CHESTPLATE);
			}
		} else {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.GOLDEN_CHESTPLATE);
			} else if (type == 1) {
				item = new ItemStack(Material.LEATHER_CHESTPLATE);
			}
		}

		return item;
	}

	private ItemStack getRandomLeggings(int level) {
		ItemStack item = null;

		if (level >= 15) {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.DIAMOND_LEGGINGS);
			} else if (type == 1) {
				item = new ItemStack(Material.IRON_LEGGINGS);
			}
		} else if (level >= 10) {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.IRON_LEGGINGS);
			} else if (type == 1) {
				item = new ItemStack(Material.GOLDEN_LEGGINGS);
			}
		} else {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.GOLDEN_LEGGINGS);
			} else if (type == 1) {
				item = new ItemStack(Material.LEATHER_LEGGINGS);
			}
		}

		return item;
	}

	private ItemStack getRandomBoots(int level) {
		ItemStack item = null;

		if (level >= 15) {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.DIAMOND_BOOTS);
			} else if (type == 1) {
				item = new ItemStack(Material.IRON_BOOTS);
			}
		} else if (level >= 10) {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.IRON_BOOTS);
			} else if (type == 1) {
				item = new ItemStack(Material.GOLDEN_BOOTS);
			}
		} else {
			int type = (int) (Math.random() * 3);
			if (type == 0) {
				item = new ItemStack(Material.GOLDEN_BOOTS);
			} else if (type == 1) {
				item = new ItemStack(Material.LEATHER_BOOTS);
			}
		}

		return item;
	}

}
