package kei.test.plugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public class Common {

	private static Common singleton = new Common();

	private Common() {
	}

	public static Common getInstance() {
		return singleton;
	}

	public void setCustomName(LivingEntity livingEntity, MobStatus mobStatus) {
		if (mobStatus == null)
			return;

		String customName = "";
		livingEntity.setCustomName(customName);

		if (livingEntity instanceof Monster) {
			customName = ChatColor.RESET + "[Lv." + mobStatus.getLevel() + "] " + ChatColor.RED
					+ MobName.getInstance().getName(livingEntity) + " ";
		} else {
			customName = ChatColor.RESET + "[Lv." + mobStatus.getLevel() + "] " + ChatColor.GREEN
					+ MobName.getInstance().getName(livingEntity) + " ";
		}

		int hp = (int) (Math.max(0, livingEntity.getHealth()));

		//		int maxHp = (int) livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		int maxHp = (int) mobStatus.getMaxHp();

		if (hp <= (double) (maxHp * 0.2)) {
			customName += ChatColor.RED + String.valueOf(hp) + ChatColor.RESET + "/" + ChatColor.GREEN
					+ String.valueOf(maxHp) + ChatColor.RED + " ❤";
		} else if (hp <= (double) (maxHp * 0.5)) {
			customName += ChatColor.YELLOW + String.valueOf(hp) + ChatColor.RESET + "/" + ChatColor.GREEN
					+ String.valueOf(maxHp) + ChatColor.RED + " ❤";
		} else {
			customName += ChatColor.GREEN + String.valueOf(hp) + ChatColor.RESET + "/" + ChatColor.GREEN
					+ String.valueOf(maxHp) + ChatColor.RED + " ❤";
		}

		livingEntity.setCustomName(customName);
		livingEntity.setCustomNameVisible(true);
	}

	//	public String getCustomNameWithStatus(LivingEntity livingEntity) {
	//		String customName = "";
	//
	//		UUID uuid = livingEntity.getUniqueId();
	//		MobStatus mobStatus = MobManager.getInstance().getStatus(uuid);
	//
	//		if (mobStatus == null) {
	//			MobManager.getInstance().add(livingEntity, new MobStatus("None", 1, 1, 1));
	//			mobStatus = MobManager.getInstance().getStatus(uuid);
	//			//			System.out.println("モブ情報がなかったので登録しました");
	//			//			System.out.println(livingEntity.getUniqueId());
	//		}
	//
	//		String name = mobStatus.getName();
	//		String level = String.valueOf(mobStatus.getLevel());
	//
	//		if (livingEntity instanceof Monster) {
	//			customName = ChatColor.RESET + "[Lv." + level + "] " + ChatColor.RED + name + " ";
	//		} else {
	//			customName = ChatColor.RESET + "[Lv." + level + "] " + ChatColor.GREEN + name + " ";
	//		}
	//
	//		int hp = (int) (Math.max(0, livingEntity.getHealth()));
	//
	//		//		int maxHp = (int) livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
	//		int maxHp = (int) (mobStatus.getHp());
	//
	//		if (hp <= (double) (maxHp * 0.2)) {
	//			customName += ChatColor.RED + String.valueOf(hp) + ChatColor.RESET + "/" + ChatColor.GREEN
	//					+ String.valueOf(maxHp) + ChatColor.RED + " ❤";
	//		} else if (hp <= (double) (maxHp * 0.5)) {
	//			customName += ChatColor.YELLOW + String.valueOf(hp) + ChatColor.RESET + "/" + ChatColor.GREEN
	//					+ String.valueOf(maxHp) + ChatColor.RED + " ❤";
	//		} else {
	//			customName += ChatColor.GREEN + String.valueOf(hp) + ChatColor.RESET + "/" + ChatColor.GREEN
	//					+ String.valueOf(maxHp) + ChatColor.RED + " ❤";
	//		}
	//
	//		livingEntity.setCustomName(customName);
	//
	//		return customName;
	//	}

}
