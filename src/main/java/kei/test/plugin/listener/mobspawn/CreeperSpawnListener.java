package kei.test.plugin.listener.mobspawn;

import java.util.List;
import java.util.UUID;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.MobManager;
import kei.test.plugin.MobStatus;

public class CreeperSpawnListener implements Listener {

	private Plugin plugin;

	private MobStatus status;

	private double speed = 0.47;

	public CreeperSpawnListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onExplodeCreeper(EntityExplodeEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof Creeper) {
			Creeper creeper = (Creeper) entity;
			UUID uuid = creeper.getUniqueId();

			// 近くのプレイヤーに雷を落とす
			List<Entity> entities = creeper.getNearbyEntities(5.0, 5.0, 5.0);
			for (Entity e : entities) {
				if (e instanceof Player) {
					Player p = (Player) e;
					p.getWorld().strikeLightningEffect(p.getLocation());
					p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 4);
				}
			}

			LivingEntity newCreeper = (LivingEntity) creeper.getWorld().spawnEntity(creeper.getLocation(),
					EntityType.CREEPER,
					SpawnReason.CUSTOM);

			MobStatus oldStatus = MobManager.getInstance().getStatus(uuid);
			MobManager.getInstance().createMobStatus(newCreeper);
			UUID newUUID = newCreeper.getUniqueId();
			MobManager.getInstance().getStatus(newUUID).setLevel(oldStatus.getLevel());
			MobManager.getInstance().getStatus(newUUID).setMaxHp(oldStatus.getMaxHp());
			MobManager.getInstance().getStatus(newUUID).setHp(oldStatus.getHp());
			MobManager.getInstance().getStatus(newUUID).setAttack(oldStatus.getAttack());

			double maxHp = creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			newCreeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHp);
			newCreeper.setHealth(creeper.getHealth());

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreeperDeathByExplosion(CreeperPowerEvent event) {
		Creeper creeper = event.getEntity();

		if (creeper.getEntitySpawnReason().equals(SpawnReason.CUSTOM)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCrepeerSpawn(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		SpawnReason reason = entity.getEntitySpawnReason();

		if (reason.equals(SpawnReason.NATURAL)
				|| reason.equals(SpawnReason.CUSTOM)
				|| reason.equals(SpawnReason.SPAWNER_EGG)) {
		} else {
			return;
		}

		if (entity.getType().equals(EntityType.CREEPER)) {
			Creeper creeper = (Creeper) entity;
			initCreeperStatus(creeper);
		}
	}

	private void initCreeperStatus(Creeper creeper) {
		creeper.setMaxFuseTicks(20);
		creeper.setExplosionRadius(5);
		creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
	}
}
