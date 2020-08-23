package kei.test.plugin.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class ChatListener implements Listener {

	public ChatListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		try {
			File file = new File("plugins/chatlog.txt");
			FileWriter filewriter = new FileWriter(file, true);

			String name = event.getPlayer().getName();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
			String time = sdf.format(timestamp);

			String msg = "[" + time + "] (" + name + "): " + event.getMessage() + "\n";
			filewriter.write(msg);
			filewriter.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
