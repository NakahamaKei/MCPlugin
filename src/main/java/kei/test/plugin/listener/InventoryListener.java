package kei.test.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class InventoryListener implements Listener {
	public InventoryListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		System.out.println(this.getClass().getName() + " is Active");
	}

	// インベントリ内アイテムをクリックしたとき
	@EventHandler
	public void onClickInventoryItem(InventoryClickEvent e) {
		String inventoryName = e.getView().getTitle();

		if (inventoryName.equalsIgnoreCase("討伐ステータス")) {
			e.setCancelled(true);
			return;
		}

	}

}
