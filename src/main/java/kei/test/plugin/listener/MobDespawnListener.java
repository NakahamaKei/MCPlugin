package kei.test.plugin.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class MobDespawnListener implements Listener {

	public MobDespawnListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onMobDespawn(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		String name = entity.getCustomName();
		if (name.equals("見習い魔導士")) {
			event.setCancelled(true);
		}

		//		MobManager.getInstance().remove(entity.getUniqueId());
		//		entity.remove();
	}
}
