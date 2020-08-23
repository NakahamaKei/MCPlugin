package kei.test.plugin.dangeonmanager.tutorial;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import kei.test.plugin.dangeonmanager.DangeonMobManager;
import kei.test.plugin.meta.MonsterNameMetaKey;
import kei.test.plugin.monster.SpawnMonster;
import kei.test.plugin.sound.SoundEffect;

public class TutorialDangeonManager extends DangeonMobManager implements Listener {

	private int mobGroup = 1;
	private static String dangeonName = "dangeons/tutorial";
	private final static World world = Bukkit.getWorld(dangeonName);
	private static BukkitTask spawnTask;
	private static BukkitTask locationTask;

	private Vector spawnLoc1 = new Vector(89, 48, 7);
	private Plugin plugin;

	private static int playerCount = 0;
	private static int countUntilBossSpawn = 15;
	private static int curCountUntilBossSpawn = 0;

	private final static ItemStack GOLD_INGOT = new ItemStack(Material.GOLD_INGOT);

	public TutorialDangeonManager(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		setSpawnLimit(2);

		int x = spawnLoc1.getBlockX();
		int y = spawnLoc1.getBlockY();
		int z = spawnLoc1.getBlockZ();

		addSpawnLocation(new Location(world, x, y, z));

		spawnMobTroops(0);

		runSpawnTask();
		runLocationTask();
		runPlayerMonitorTask();
	}

	private void runPlayerMonitorTask() {
		new BukkitRunnable() {

			@Override
			public void run() {
				playerCount = world.getPlayerCount();
				if (playerCount > 0) {
					if (spawnTask.isCancelled()) {
						runSpawnTask();
					}
					if (locationTask.isCancelled()) {
						runLocationTask();
					}
				} else {
					spawnTask.cancel();
					locationTask.cancel();
				}
			}
		}.runTaskTimer(plugin, 0L, 20L * 10);
	}

	// プレイヤーがダンジョンに居るときだけモブを沸かせる
	@EventHandler
	public void onJoinDangeon(PlayerTeleportEvent event) {
		// ダンジョンに入るとき
		World toWorld = event.getTo().getWorld();
		if (toWorld.equals(world)) {
			event.getPlayer().setBedSpawnLocation(world.getSpawnLocation(), true);

			if (playerCount >= 0) {
				if (spawnTask.isCancelled()) {
					runSpawnTask();
				}

				if (locationTask.isCancelled()) {
					runLocationTask();
				}
			}

			playerCount++;
			return;
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();

		if (!player.getWorld().equals(world)) {
			return;
		}

		player.teleport(world.getSpawnLocation());
	}

	@EventHandler
	public void onDigOre(BlockBreakEvent event) {
		Player player = event.getPlayer();
		World w = player.getWorld();
		Block b = event.getBlock();
		if (!w.equals(world)) {
			return;
		}

		if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
			return;
		}

		event.setCancelled(true);
		if (b.getType().equals(Material.GOLD_ORE)) {
			if (player.getInventory().containsAtLeast(GOLD_INGOT, 64)) {
				player.sendMessage("これ以上持つことは出来ません");
				SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ITEM_BREAK, 5);
				return;
			}

			Location loc = event.getBlock().getLocation();
			event.getBlock().setType(Material.COBBLESTONE);
			player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ITEM_PICKUP, 7);

			new BukkitRunnable() {
				@Override
				public void run() {
					loc.getBlock().setType(Material.GOLD_ORE);
					this.cancel();
				}
			}.runTaskLater(plugin, 20L * 20);
		}

	}

	private void runSpawnTask() {
		spawnTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (world.getPlayerCount() == 0) {
					this.cancel();
				}

				for (int i = 0; i < mobGroup; i++) {
					for (int j = 0; j < getSpawnLimit(); j++) {
						if (canSpawn(i)) {
							spawnMobTroops(i);
						}
					}
				}

			}
		}.runTaskTimer(plugin, 0L, 20L * 15);
	}

	private boolean canSpawn(int groupIndex) {
		int count = getMobCount(groupIndex);
		return count < getSpawnLimit();
	}

	private void spawnMobTroops(int locationIndex) {
		Location spawnLoc = getSpawnLocation(locationIndex);

		//		int randX = (int) (Math.random() * 2) + 1;
		//		int rand;
		//		rand = (int) (Math.random() * 2);
		//		if (rand == 0) {
		//			randX *= -1;
		//		}
		//
		//		int randZ = (int) (Math.random() * 2) + 1;
		//		rand = (int) (Math.random() * 2);
		//		if (rand == 0) {
		//			randZ *= -1;
		//		}
		//
		//		spawnLoc.add(randX, 0.0, randZ);

		//		LivingEntity entity = (LivingEntity) world.spawnEntity(spawnLoc,
		//				EntityType.ZOMBIE, SpawnReason.CUSTOM);
		LivingEntity entity = SpawnMonster.spawnMonster(world, spawnLoc, EntityType.ZOMBIE, 3, plugin,
				MonsterNameMetaKey.ROCK_ZOMBIE);

		SoundEffect.getInstance().playSoundEffect(entity, Sound.ENTITY_ZOMBIE_INFECT, 20);

		Zombie z = (Zombie) entity;
		z.setBaby(false);

		z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60000, 1));
		this.setCustomEquipment(z);

		addMobIntoGroup(locationIndex, entity);
	}

	@EventHandler
	public void onPickItem(EntityPickupItemEvent event) {
		LivingEntity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}

		if (!entity.getWorld().equals(world)) {
			return;
		}

		Player player = (Player) entity;
		if (player.getInventory().containsAtLeast(GOLD_INGOT, 64)) {
			event.getItem().remove();
			player.getInventory().remove(Material.GOLD_INGOT);
			player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (!player.getWorld().equals(world)) {
			return;
		}

		Material main = player.getInventory().getItemInMainHand().getType();
		Material sub = player.getInventory().getItemInOffHand().getType();
		if (player.getGameMode().equals(GameMode.SURVIVAL) && (main.equals(Material.WATER_BUCKET)
				|| main.equals(Material.LAVA_BUCKET)
				|| sub.equals(Material.WATER_BUCKET)
				|| sub.equals(Material.LAVA_BUCKET))) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld().equals(world)
				&& player.getGameMode().equals(GameMode.SURVIVAL)) {
			event.setCancelled(true);
		}
	}

	private void setCustomEquipment(Monster z) {
		ItemStack item;
		LeatherArmorMeta meta;

		item = new ItemStack(Material.LEATHER_HELMET, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.GRAY);
		item.setItemMeta(meta);
		z.getEquipment().setHelmet(item);
		z.getEquipment().setHelmetDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.GRAY);
		item.setItemMeta(meta);
		z.getEquipment().setChestplate(item);
		z.getEquipment().setChestplateDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.GRAY);
		item.setItemMeta(meta);
		z.getEquipment().setLeggings(item);
		z.getEquipment().setLeggingsDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_BOOTS, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.GRAY);
		item.setItemMeta(meta);
		z.getEquipment().setBoots(item);
		z.getEquipment().setBootsDropChance(0.0f);

		item = new ItemStack(Material.WOODEN_HOE, 1);
		z.getEquipment().setItemInMainHand(item);
		z.getEquipment().setItemInOffHandDropChance(0.0f);
	}

	private void runLocationTask() {
		locationTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (world.getPlayerCount() == 0) {
					this.cancel();
				}

				for (int i = 0; i < mobGroup; i++) {
					for (int j = 0; j < getMobCount(i); j++) {
						boolean isAlive = getMob(i, j).isValid();
						if (!isAlive) {
							removeMobFromGroup(i, j);
							continue;
						}

						// スポーン地点よりも20ブロック以上離れたらもとに戻る
						LivingEntity entity = getMob(i, j);
						Location spawnLocation = getSpawnLocation(i);
						if (entity != null && spawnLocation != null) {
							double distanceFromSpawn = entity.getLocation().distanceSquared(spawnLocation);
							if (distanceFromSpawn >= 400) {
								entity.teleport(spawnLocation);
							}
						}
					}
				}
			}

		}.runTaskTimer(JavaPlugin.getProvidingPlugin(TutorialDangeonManager.class), 0L, 20L * 10);
	}
}
