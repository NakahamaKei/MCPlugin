package kei.test.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class GetExpBottleListener implements Listener {

	private final String mini = "ミニボトル";
	private final String normal = "ミディアムボトル";
	private final String large = "ラージボトル";
	private final String titan = "タイタンボトル";

	public GetExpBottleListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onBreakExpBottle(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action.equals(Action.LEFT_CLICK_AIR)
				|| action.equals(Action.LEFT_CLICK_BLOCK)) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item == null) {
			return;
		}

		ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();

		if (itemMeta == null) {
			return;
		}

		String itemName = itemMeta.getDisplayName();

		if (itemName.contains(mini)) {
			player.giveExp(550, true);
			item.setAmount(Math.max(0, item.getAmount() - 1));
		} else if (itemName.contains(normal)) {
			player.giveExp(1395, true);
			item.setAmount(Math.max(0, item.getAmount() - 1));
		} else if (itemName.contains(large)) {
			player.giveExp(5345, true);
			item.setAmount(Math.max(0, item.getAmount() - 1));
		} else if (itemName.contains(titan)) {
			player.giveExp(12000, true);
			item.setAmount(Math.max(0, item.getAmount() - 1));
		}
	}
}
