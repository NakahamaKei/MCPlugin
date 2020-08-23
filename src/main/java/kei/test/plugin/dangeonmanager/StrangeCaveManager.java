package kei.test.plugin.dangeonmanager;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import kei.test.plugin.meta.MonsterNameMetaKey;
import kei.test.plugin.monster.MonsterName;
import kei.test.plugin.monster.SpawnMonster;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class StrangeCaveManager extends DangeonMobManager implements Listener {

	private int mobGroup = 1;
	private static String dangeonName = "dangeons/dangeon_strange_cave";
	private static World world = Bukkit.getWorld(dangeonName);
	private static BukkitTask spawnTask;
	private static BukkitTask locationTask;

	private Vector spawnLoc1 = new Vector(6, 17, 73);
	private Plugin plugin;

	private static int playerCount = 0;
	private static int countUntilBossSpawn = 15;
	private static int curCountUntilBossSpawn = 0;

	public StrangeCaveManager(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		setSpawnLimit(6);

		int x = spawnLoc1.getBlockX();
		int y = spawnLoc1.getBlockY();
		int z = spawnLoc1.getBlockZ();

		addSpawnLocation(new Location(world, x, y, z));

		spawnMobTroops(0);

		runSpawnTask();
		runLocationTask();
	}

	//	@EventHandler
	//	public void onMobSpawn(CreatureSpawnEvent event) {
	//		SpawnReason reason = event.getEntity().getEntitySpawnReason();
	//
	//	}

	@EventHandler
	public void onKillMob(EntityDeathEvent event) {
		LivingEntity deadEntity = event.getEntity();
		Player killer = event.getEntity().getKiller();

		if (!world.equals(deadEntity.getWorld())) {
			return;
		}

		if (killer != null) {
			killer.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, getRandomNum()));
			SoundEffect.getInstance().playSoundEffect(killer, Sound.ENTITY_ITEM_PICKUP, 1);
			curCountUntilBossSpawn++;

			if (countUntilBossSpawn <= curCountUntilBossSpawn) {
				Location loc = getSpawnLocation(0);
				world.strikeLightningEffect(loc);

				LivingEntity entity = SpawnMonster.spawnMonster(world, loc, EntityType.ZOMBIE, 20,
						plugin, MonsterNameMetaKey.BOSS_LAPIS_ZOMBIE);
				Zombie z = (Zombie) entity;
				z.setBaby(false);

				z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60000, 2));
				this.setBossCustomEquipment(z);

				Collection<Player> players = z.getLocation().getNearbyPlayers(50);
				for (Player p : players) {
					p.sendActionBar(ChatColor.RED + "ボスが出現しました");
				}

				addMobIntoGroup(0, entity);
				curCountUntilBossSpawn = 0;
			}

			if (deadEntity.getName().contains(MonsterName.HEAVY_LAPIS_ZOMBIE)) {
				Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + ChatColor.GOLD + "が"
						+ MonsterName.HEAVY_LAPIS_ZOMBIE + "を討伐しました");
				SoundEffect.getInstance().playSoundAllPlayers(Sound.ENTITY_PLAYER_LEVELUP);
			}
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
		}.runTaskTimer(JavaPlugin.getProvidingPlugin(StrangeCaveManager.class), 0L, 20L * 60);
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

		}.runTaskTimer(JavaPlugin.getProvidingPlugin(StrangeCaveManager.class), 0L, 20L * 10);
	}

	// プレイヤーがダンジョンに居るときだけモブを沸かせる
	@EventHandler
	public void onJoinDangeon(PlayerTeleportEvent event) {
		// ダンジョンに入るとき
		World toWorld = event.getTo().getWorld();
		if (toWorld.equals(world)) {
			if (playerCount >= 0) {
				runSpawnTask();
				runLocationTask();
			}

			playerCount++;
			return;
		}

		// ダンジョン出るとき
		World fromWorld = event.getFrom().getWorld();
		if (fromWorld.equals(world)) {
			playerCount = Math.max(0, playerCount - 1);

			// プレイヤーがいないならモブの生成を止める
			if (playerCount <= 0) {
				spawnTask.cancel();
				locationTask.cancel();
			}
			return;
		}
	}

	@EventHandler
	public void onDigOre(BlockBreakEvent event) {
		Player player = event.getPlayer();
		World w = player.getWorld();
		Block b = event.getBlock();
		if (!w.equals(world)) {
			return;
		}

		if (!player.getGameMode().equals(GameMode.SURVIVAL)
				|| b.getType().equals(Material.CHEST)) {
			return;
		}

		event.setCancelled(true);
		if (b.getType().equals(Material.LAPIS_ORE)) {
			Location loc = event.getBlock().getLocation();
			event.getBlock().setType(Material.COBBLESTONE);
			player.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, this.getRandomNum()));
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ITEM_PICKUP, 7);

			new BukkitRunnable() {
				@Override
				public void run() {
					loc.getBlock().setType(Material.LAPIS_ORE);
					this.cancel();
				}
			}.runTaskLater(JavaPlugin.getProvidingPlugin(StrangeCaveManager.class), 20L * 20);
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

	private int getRandomNum() {
		int rand = (int) (Math.random() * 2) + 1;
		return rand;
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

		LivingEntity entity = (LivingEntity) world.spawnEntity(spawnLoc,
				EntityType.ZOMBIE, SpawnReason.CUSTOM);

		SoundEffect.getInstance().playSoundEffect(entity, Sound.ENTITY_ZOMBIE_INFECT, 20);

		entity.setMetadata(MonsterNameMetaKey.LAPIS_ZOMBIE, new FixedMetadataValue(plugin, true));
		Zombie z = (Zombie) entity;
		z.setBaby(false);

		z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60000, 1));
		z.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 60000, 2));
		this.setCustomEquipment(z);

		addMobIntoGroup(locationIndex, entity);
	}

	private void setCustomEquipment(Monster z) {
		ItemStack item;
		LeatherArmorMeta meta;

		item = new ItemStack(Material.LEATHER_HELMET, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setHelmet(item);
		z.getEquipment().setHelmetDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setChestplate(item);
		z.getEquipment().setChestplateDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setLeggings(item);
		z.getEquipment().setLeggingsDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_BOOTS, 1);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setBoots(item);
		z.getEquipment().setBootsDropChance(0.0f);

		item = new ItemStack(Material.WOODEN_SWORD, 1);
		z.getEquipment().setItemInMainHand(item);
		z.getEquipment().setItemInOffHandDropChance(0.0f);
	}

	private void setBossCustomEquipment(Monster z) {
		ItemStack item;
		LeatherArmorMeta meta;

		item = new ItemStack(Material.LEATHER_HELMET, 1);
		item.addEnchantment(EnchantmentWrapper.PROTECTION_ENVIRONMENTAL, 2);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setHelmet(item);
		z.getEquipment().setHelmetDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		item.addEnchantment(EnchantmentWrapper.PROTECTION_ENVIRONMENTAL, 2);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setChestplate(item);
		z.getEquipment().setChestplateDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		item.addEnchantment(EnchantmentWrapper.PROTECTION_ENVIRONMENTAL, 2);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setLeggings(item);
		z.getEquipment().setLeggingsDropChance(0.0f);

		item = new ItemStack(Material.LEATHER_BOOTS, 1);
		item.addEnchantment(EnchantmentWrapper.PROTECTION_ENVIRONMENTAL, 2);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.BLUE);
		item.setItemMeta(meta);
		z.getEquipment().setBoots(item);
		z.getEquipment().setBootsDropChance(0.0f);

		item = new ItemStack(Material.IRON_SWORD, 1);
		z.getEquipment().setItemInMainHand(item);
		z.getEquipment().setItemInOffHandDropChance(0.0f);
	}
}
