package kei.test.plugin.listener;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;

public class FishingListener implements Listener {

	public FishingListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onFishing(PlayerFishEvent event) {
		Entity entity = event.getCaught();

		int rand = (int) (Math.random() * 10) + 1;

		if (rand == 5) {
			Location loc = event.getPlayer().getLocation();
			BlockFace blockFace = event.getPlayer().getFacing();
			int x = loc.getBlockX() + blockFace.getModX();
			int y = loc.getBlockX() + blockFace.getModX();
			int z = loc.getBlockX() + blockFace.getModX();
		}
	}
}
