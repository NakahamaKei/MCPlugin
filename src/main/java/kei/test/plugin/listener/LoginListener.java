package kei.test.plugin.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.PlayerInfo;
import kei.test.plugin.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class LoginListener implements Listener {

	public LoginListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	// ログイン時
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);
		if (playerInfo == null) {
			PlayerManager.getInstance().registerPlayer(uuid);
		}

		String worldName = event.getPlayer().getWorld().getName();
		if (worldName.equals("world") || worldName.equals("world_nether") || worldName.equals("world_the_end")
				|| worldName.equals("dangeons/tutorial")) {
			// 何もしない
		} else {
			Location homeLoc = Bukkit.getWorld("world").getSpawnLocation();
			event.getPlayer().teleport(homeLoc);
		}

		Player player = event.getPlayer();
		if (!event.getPlayer().hasPlayedBefore()) {
			player.teleport(Bukkit.getWorld("dangeons/tutorial").getSpawnLocation());
			player.sendMessage(ChatColor.GOLD + "MORPG風の生活サーバーへようこそ！");
			player.sendMessage(ChatColor.GOLD + "当サーバーに遊びに来て頂いてありがとうございます！");
			player.sendMessage(ChatColor.GOLD + "ここはチュートリアルワールドです。アイテムの買い方を教わったりこの世界で出現するモンスターと対峙することができます");
			player.sendMessage(ChatColor.GOLD + "まずは目の前にいるNPCに右クリックで話しかけてみましょう");
			player.sendMessage(ChatColor.GOLD + "チュートリアルをスキップして生活ワールドへ行く場合はチャットで /join world と打ってください");
			return;
		} else {
			event.getPlayer().sendMessage("========================================================");
			event.getPlayer().sendMessage("MORPGサーバーへようこそ！ゆっくりしていってね！");
			event.getPlayer().sendMessage("[tips] /command 利用可能なコマンドを表示");
			event.getPlayer().sendMessage("[tips] /init 初期リスポーン地点にテレポートする");
			event.getPlayer().sendMessage("[tips] /home 最後に登録したベッドにテレポートする");
			event.getPlayer().sendMessage("↓管理人に連絡がある場合はこちらから↓");
			event.getPlayer().sendMessage("https://twitter.com/non79562074");
			event.getPlayer().sendMessage("↓１日１回投票をお願いします！↓");
			event.getPlayer().sendMessage("https://minecraft.jp/servers/118.27.13.89");
			event.getPlayer().sendMessage("========================================================");
			return;
		}
	}

	// ログアウト時
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);
		if (playerInfo != null) {

			if (playerInfo.getPortal() != null) {
				playerInfo.getPortal().breakPortal();
			}

			PlayerManager.getInstance().unregisterPlayer(uuid);
		}
	}

}
