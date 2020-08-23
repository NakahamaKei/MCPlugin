package kei.test.plugin.listener.mobspawn;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VindicatorSpawnListener implements Listener {

	private LivingEntity entity;

	public VindicatorSpawnListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onVindicatorSpawn(CreatureSpawnEvent event) {

		entity = event.getEntity();

		SpawnReason reason = event.getSpawnReason();
		EntityType type = entity.getType();

		if (!reason.equals(SpawnReason.CUSTOM)
				|| !type.equals(EntityType.VINDICATOR)) {
			return;
		}

		if (!entity.getWorld().getName().equalsIgnoreCase("world"))
			return;

		entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 500, 2));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 100, 1));

		int kind = (int) (Math.random() * 4) + 1;
		switch (kind) {
		case 1:
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.WOODEN_HOE));
			break;
		case 2:
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));
			break;
		case 3:
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.WOODEN_AXE));
			break;
		case 4:
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
			break;
		default:
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.WOODEN_HOE));
			break;
		}

		int chance = (int) (Math.random() * 10) + 1;
		if (1 <= chance && chance <= 2) {
			entity.getEquipment().setItemInOffHand(new ItemStack(Material.DIAMOND));
			entity.getEquipment().setItemInOffHandDropChance(1.0F);
		}

		return;
	}

}
