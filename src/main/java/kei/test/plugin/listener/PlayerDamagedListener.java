package kei.test.plugin.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class PlayerDamagedListener implements Listener {

	public PlayerDamagedListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerDamaged(EntityDamageByEntityEvent event) {

		Entity entity = event.getDamager();

		if (entity instanceof Creeper && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			event.setCancelled(true);
			if (!(event.getEntity() instanceof Player)) {
				return;
			}

			double damage = ((Creeper) entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue();
			event.setDamage(damage * 2.0);
			double resDamage = event.getFinalDamage();
			player.damage(resDamage);
		}
	}
}
