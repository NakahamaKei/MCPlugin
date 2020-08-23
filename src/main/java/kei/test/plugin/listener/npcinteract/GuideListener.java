package kei.test.plugin.listener.npcinteract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class GuideListener implements Listener {

	private final String guideName = ChatColor.AQUA + "隻眼の魔導士";

	private final List<String> speeches = new ArrayList<>();

	private final String dangeonName = "不気味な洞窟";

	public GuideListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		speeches.add(makeSpeech("よくここを見つけられたな。相応の力のあるものとお見受けする"));
		speeches.add(makeSpeech("死にたくなければ相当の装備を身に着けてからくるんだな"));
		speeches.add(makeSpeech("ダンジョンはクリーチャーの巣窟だ、用心して行け"));
	}

	@EventHandler
	public void onTalkGuide(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)
				&& event.getHand().equals(EquipmentSlot.HAND)
				&& event.getRightClicked().getCustomName().equals(guideName)) {

			String speech = getRandomSpeech();
			player.sendMessage(speech);
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_WITCH_CELEBRATE, 5);

			ItemStack dangeonIcon = createNewItem(dangeonName, Material.ZOMBIE_HEAD,
					Arrays.asList("危険な臭いがする洞窟、奥地まで調査求ム", ChatColor.GOLD + "採掘可能アイテム", ChatColor.GREEN + "ラピスラズリ"));

			Inventory inventory = Bukkit.createInventory(null, 9, "危険区域リスト");
			inventory.setItem(0, dangeonIcon);

			for (int i = 1; i < 9; i++) {
				inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
			}

			player.openInventory(inventory);

			return;
		}
	}

	@EventHandler
	public void onClickDangeonIcon(InventoryClickEvent event) {
		String name = event.getView().getTitle();

		if (name.equalsIgnoreCase("危険区域リスト")) {
			event.setCancelled(true);

			Player player = (Player) event.getWhoClicked();

			Location loc = Bukkit.getWorld("dangeons/dangeon_strange_cave").getSpawnLocation();
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5);
			player.teleport(loc);

			String message = makeDangeonJoinMessage(player.getName());
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				onlinePlayer.sendMessage(message);
				SoundEffect.getInstance().playSoundEffect(onlinePlayer, Sound.ENTITY_PLAYER_LEVELUP, 1);
			}

			return;
		}
	}

	private ItemStack createNewItem(String name, Material material, List<String> lores) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + name);

		if (lores != null) {
			for (int i = 0; i < lores.size(); i++) {
				lores.set(i, ChatColor.WHITE + lores.get(i));
			}
			meta.setLore(lores);
		}
		item.setItemMeta(meta);

		return item;
	}

	private String makeSpeech(String speech) {
		speech = ChatColor.AQUA + "[" + guideName + "]: " + ChatColor.GOLD + speech;
		return speech;
	}

	private String getRandomSpeech() {
		int size = speeches.size();
		int i = (int) (Math.random() * size);

		return speeches.get(i);
	}

	private String makeDangeonJoinMessage(String name) {
		String message = ChatColor.AQUA + name + ChatColor.YELLOW + "が、危険区域「" + ChatColor.RED + dangeonName
				+ ChatColor.YELLOW
				+ "」に侵入しました。";
		return message;
	}

}
