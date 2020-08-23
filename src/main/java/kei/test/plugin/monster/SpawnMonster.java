package kei.test.plugin.monster;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.Common;
import kei.test.plugin.MobManager;
import kei.test.plugin.MobStatus;
import kei.test.plugin.PlayerManager;
import kei.test.plugin.sound.SoundEffect;

public class SpawnMonster {
	public static LivingEntity spawnMonster(World world, Location loc, EntityType type, int level, Plugin plugin,
			String mobNameMeta) {
		LivingEntity entity = (LivingEntity) world.spawnEntity(loc, type, SpawnReason.CUSTOM);
		entity.getUniqueId();

		SoundEffect.getInstance().playSoundEffect(entity, Sound.ENTITY_ZOMBIE_AMBIENT, 15);

		//		MobManager.getInstance().createMonsterStatus(entity, level);
		MobStatus status = MobManager.getInstance().createMonsterStatus(entity, level);
		MobStatus status2 = MobManager.getInstance().getStatus(status.getUuid());

		Collection<Player> players = loc.getNearbyPlayers(100);

		for (Player p : players) {
			PlayerManager.getInstance().getPlayerInfo(p.getUniqueId()).addMobStatus(status);
		}
		entity.setMetadata(mobNameMeta, new FixedMetadataValue(plugin, true));

		Common.getInstance().setCustomName(entity, status);
		return entity;
	}
}
