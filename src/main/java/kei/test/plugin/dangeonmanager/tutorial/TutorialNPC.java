package kei.test.plugin.dangeonmanager.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.items.ItemCreator;
import kei.test.plugin.items.WeaponName;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class TutorialNPC implements Listener {

	private final List<String> speeches = new ArrayList<>();

	private Plugin plugin;

	public TutorialNPC(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;

		speeches.add(makeSpeech("ようこそ、初めまして。新しくこの世界に落ちてきた人だね。よろしく！", NPCName.TUTORIAL_NPC_1));
		speeches.add(makeSpeech("最近この世界では凶悪なモンスターが大量に出現するようになっちゃったんだ。だからまず十分な装備を整えてね！", NPCName.TUTORIAL_NPC_1));
		speeches.add(
				makeSpeech("この先に武器ショップがあるから、まずはそこで装備を買っていってね。購入には金が必要になるから、そこらへんで掘っていってね！", NPCName.TUTORIAL_NPC_1));
		speeches.add(makeSpeech("ここまで話を聞いてくれてありがとう、お礼に" + WeaponName.TUTORIAL_PICKAXE + "をプレゼントするよ！",
				NPCName.TUTORIAL_NPC_1));
		speeches.add(makeSpeech("この先は危険なモンスターが出現するから気を付けてね", NPCName.TUTORIAL_NPC_1));
	}

	@EventHandler
	public void onTalkTipsGuide(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		int index1 = 0;
		if (!player.hasMetadata("index1")) {
			player.setMetadata("index1", new FixedMetadataValue(plugin, 0));
		} else {
			index1 = player.getMetadata("index1").get(0).asInt();
		}

		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)
				&& event.getHand().equals(EquipmentSlot.HAND)) {

			if (event.getRightClicked().getCustomName().equals(NPCName.TUTORIAL_NPC_1)) {
				event.setCancelled(true);

				player.sendMessage(speeches.get(index1));
				if (speeches.get(index1).contains(WeaponName.TUTORIAL_PICKAXE)) {
					givePlayerTool(player);
					player.sendMessage(ChatColor.GREEN + "鉄のピッケルを入手した");
				}

				index1++;
				if (index1 == speeches.size()) {
					index1 = speeches.size() - 1;
				}

				player.setMetadata("index1", new FixedMetadataValue(plugin, index1));
				SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_VILLAGER_AMBIENT, 5);
				return;
			}

			return;
		}
	}

	private String makeSpeech(String speech, String name) {
		speech = ChatColor.AQUA + "[" + name + "]: " + ChatColor.GOLD + speech;
		return speech;
	}

	// プレイヤーにツールを与える
	private void givePlayerTool(Player player) {
		ItemMeta meta;
		String name;
		for (ItemStack content : player.getInventory().getContents()) {
			if (content == null || content.getType().equals(Material.AIR)) {
				continue;
			}

			meta = content.getItemMeta();
			if (meta == null) {
				continue;
			}

			name = meta.getDisplayName();
			if (name.equals(WeaponName.TUTORIAL_PICKAXE)) {
				player.getInventory().remove(content);
				break;
			}
		}

		ItemStack item = ItemCreator.create("アイアンピッケル", Material.IRON_PICKAXE, 1,
				Arrays.asList("鉄で出来た鋭いピッケル", "鉱石採掘にはもちろん襲い掛かってくるモンスターも", "頑張れば撃退できる、かもしれない"));
		player.getInventory().addItem(item);

		SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_PLAYER_LEVELUP, 5);
		SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ITEM_PICKUP, 5);
	}

}
