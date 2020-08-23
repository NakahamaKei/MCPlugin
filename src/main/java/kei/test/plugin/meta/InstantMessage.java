package kei.test.plugin.meta;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

// アイテム中央上にインスタントメッセージを表示する
public class InstantMessage {
	// 秒数指定あり版
	public static void showInstantMessage(Player player, String message, long span) {
		new BukkitRunnable() {
			private int count = 0;

			@Override
			public void run() {
				if (count > 0) {
					this.cancel();
				}
				player.sendActionBar(ChatColor.RED + message);
				count++;
			}
		}.runTaskTimer(JavaPlugin.getProvidingPlugin(InstantMessage.class), 0, 20 * span);
	}

	// 秒数指定なし版
	public static void showInstantMessage(Player player, String message) {
		player.sendActionBar(ChatColor.RED + message);
	}
}
