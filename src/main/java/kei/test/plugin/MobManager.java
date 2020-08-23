package kei.test.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MobManager {
	private static MobManager singleton = new MobManager();
	//	private HashMap<UUID, LivingEntity> mobMap = new HashMap<>();
	private static HashMap<UUID, MobStatus> statusMap = new HashMap<>();
	private static ArrayList<UUID> uuidList = new ArrayList<UUID>();

	private MobManager() {
		new BukkitRunnable() {
			@Override
			public void run() {
				MobManager.getInstance().clearInvalidMobStatus();
				//				if (!this.isCancelled()) {
				//					this.cancel();
				//				}

				//				System.out.println("Mob amount: " + uuidList.size());
				//				System.out.println("モブマップサイズ: " + getMobAmount());
				//								try {
				//					File file = new File("plugins/MobNum.txt");
				//					FileWriter filewriter = new FileWriter(file, true);
				//
				//					filewriter.write(String.valueOf(uuidList.size()));
				//					filewriter.close();
				//
				//				} catch (IOException e) {
				//					System.out.println(e);
				//				}
			}
		}.runTaskTimer(JavaPlugin.getProvidingPlugin(MobManager.class), 0L, 20L * 10);
	}

	public static MobManager getInstance() {
		return singleton;
	}

	public int getMobAmount() {
		return uuidList.size();
	}

	// モブ情報とステータス情報を追加
	synchronized public void add(UUID uuid, MobStatus mobStatus) {
		if (statusMap.get(uuid) == null) {
			uuidList.add(uuid);
			statusMap.put(uuid, mobStatus);
		}
	}

	// モブ情報とステータス情報を追加
	synchronized public void add(UUID uuid, MobStatus mobStatus, boolean force) {
		if (statusMap.get(uuid) == null || force) {
			uuidList.add(uuid);
			statusMap.put(uuid, mobStatus);
		}
	}

	// モブ情報とステータス情報を削除
	synchronized public void remove(UUID uuid) {
		if (statusMap.containsKey(uuid)) {
			statusMap.remove(uuid);
		}

		for (int i = 0; i < uuidList.size(); i++) {
			if (uuidList.get(i) != null && uuidList.get(i).equals(uuid)) {
				uuidList.remove(i);
				break;
			}
		}
	}

	public HashMap<UUID, MobStatus> getStatusMap() {
		return statusMap;
	}

	public MobStatus getStatus(UUID uuid) {
		return statusMap.get(uuid);
	}

	// 定期的にモブを削除する
	synchronized public void clearInvalidMobStatus() {
		if (uuidList.isEmpty())
			return;

		// モブ情報を削除する
		for (int i = 0; i < uuidList.size(); i++) {
			UUID uuid = uuidList.get(i);
			MobStatus status = statusMap.get(uuid);
			if (status == null) {
				continue;
			}

			LivingEntity entity = status.getEntity();
			if (entity == null) {
				continue;
			}

			// 近くにプレイヤーがいなければモブを削除する
			boolean check = false;
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				World world = p.getWorld();

				if (entity.getWorld().getName().equalsIgnoreCase(world.getName())) {
					int distance = (int) entity.getLocation().distanceSquared(p.getLocation());
					int height = (int) Math.abs(entity.getLocation().getBlockY() - p.getLocation().getBlockY());

					//				System.out.println(entity.getName());
					if (distance < 3600 && height <= 15) {
						check = true;
						break;
					}
				}
			}

			boolean hasNameTag = false;
			if (entity.hasMetadata("hasNameTag")) {
				hasNameTag = entity.getMetadata("hasNameTag").get(0).asBoolean();
			}

			//			ItemStack mobItem = entity.getEquipment().getItemInMainHand();
			//			boolean hasEnchant = false;
			//			if (mobItem != null) {
			//				ItemMeta meta = mobItem.getItemMeta();
			//				if (meta != null) {
			//					hasEnchant = mobItem.getItemMeta().hasEnchants();
			//				}
			//			}

			// モブを削除する
			if (!check) {
				if (entity instanceof Monster
						&& !hasNameTag) {
					//					String name = entity.getCustomName();
					//					if (name != null && !name.equals("見習い魔導士")) {
					//						entity.remove();
					//					}
					entity.remove();
				}
				statusMap.remove(uuid);
				uuidList.remove(i);
			}
		}
	}

	synchronized public MobStatus createMonsterStatus(LivingEntity livingEntity, int level) {
		if (!(livingEntity instanceof Monster)) {
			return null;
		}

		UUID uuid;
		String name = "";
		double maxHp = 1.0;
		double hp = 1.0;
		double attack = 0.0;

		uuid = livingEntity.getUniqueId();
		name = livingEntity.getType().name();

		// 最大HP設定
		maxHp = MobLevel.getInstance().getMobHP(level);
		livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHp);

		// 攻撃力設定
		attack = MobLevel.getInstance().getMobAttack(level);
		livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
				.setBaseValue(attack);

		// 現在HPを最大HPで初期化
		hp = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		livingEntity.setHealth(hp);

		// 新しくモブステータスを生成する
		MobStatus mobStatus = new MobStatus(uuid, name, level, maxHp, hp, attack, livingEntity);

		// 配列にも保存する
		this.add(uuid, mobStatus);

		return mobStatus;
	}

	// 新しいモブを生成する(排他制御)
	synchronized public MobStatus createMobStatus(LivingEntity livingEntity) {

		int level = 1;
		double attack = 0.0;
		if (livingEntity instanceof Monster) {
			if (livingEntity.getWorld().getName().equalsIgnoreCase("world_nether")
					|| livingEntity.getWorld().getName().equalsIgnoreCase("world_the_end")) {
				level = (int) (Math.random() * 10) + 11;
			} else {
				level = (int) (Math.random() * 10) + 1;
			}

			// 攻撃力設定
			attack = MobLevel.getInstance().getMobAttack(level);
			livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
					.setBaseValue(attack);
		}

		// 体力設定
		double maxHp = MobLevel.getInstance().getMobHP(level);
		livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHp);

		double hp = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		livingEntity.setHealth(hp);

		UUID uuid = livingEntity.getUniqueId();
		String name = livingEntity.getType().name();

		// 新しくモブステータスを生成する
		MobStatus mobStatus = new MobStatus(uuid, name, level, maxHp, hp, attack, livingEntity);

		// 配列にも保存する
		this.add(uuid, mobStatus);

		return mobStatus;
	}

}
