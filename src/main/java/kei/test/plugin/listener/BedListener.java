package kei.test.plugin.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import kei.test.plugin.Bed;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BedListener implements Listener {

	private List<BukkitTask> taskList = new ArrayList<>();

	public BedListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	// ベッドで眠れないようにする
	@EventHandler
	public void onClickBed(PlayerInteractEvent event) {

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

			Material material = event.getClickedBlock().getType();
			if (Bed.getInstance().isBed(material)) {

				// スポーン地点設定

				// アクションバーにメッセ表示
				Player player = event.getPlayer();
				taskList.add(new BukkitRunnable() {
					private int count = 0;

					@Override
					public void run() {
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("スポーン地点を変更しました"));

						if (taskList.size() > 1) {
							taskList.get(0).cancel();
							taskList.remove(0);
						}
						count++;

						if (count >= 5) {
							this.cancel();
						}
					}
				}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0L, 20L));
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
	}

	@EventHandler
	public void onEnterBed(PlayerBedEnterEvent event) {
		long time = event.getPlayer().getWorld().getTime();

		if (!(0 < time && time < 12300)) {
			Player player = event.getPlayer();
			player.setStatistic(Statistic.TIME_SINCE_REST, 0);
			player.sendMessage("ファントム出現までの日数をリセットしました");
			taskList.add(new BukkitRunnable() {
				private int count = 0;

				@Override
				public void run() {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("寝ることはできません"));

					if (taskList.size() > 1) {
						taskList.get(0).cancel();
						taskList.remove(0);
					}
					count++;

					if (count >= 5) {
						this.cancel();
					}
				}
			}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0L, 20L));
		}
		event.setCancelled(true);
	}
}
