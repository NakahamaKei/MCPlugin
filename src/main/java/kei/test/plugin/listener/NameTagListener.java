package kei.test.plugin.listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.database.NameTagMobsDB;

public class NameTagListener implements Listener {

	private Plugin plugin;

	public NameTagListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onNameTag(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		Entity entity = event.getRightClicked();
		if (item.getType().equals(Material.NAME_TAG)
				&& entity instanceof LivingEntity
				&& !entity.hasMetadata("hasNameTag")) {
			entity.setMetadata("hasNameTag", new FixedMetadataValue(plugin, true));

			// ネームタグを付加したのでDBに登録
			NameTagMobsDB.getInstance().setHasNameTag(entity.getUniqueId());
		}
	}
}
