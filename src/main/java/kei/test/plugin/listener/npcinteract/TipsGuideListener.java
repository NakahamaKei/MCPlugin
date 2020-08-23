package kei.test.plugin.listener.npcinteract;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class TipsGuideListener implements Listener {

	private final String name = ChatColor.AQUA + "物知りな村人";

	private final List<String> speeches = new ArrayList<>();

	public TipsGuideListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		speeches.add(makeSpeech("ストレイの弓には生物を引き寄せる効果があるそうだ。遠くにいても油断はできないぞ"));
		speeches.add(makeSpeech("魔導士の家の地下には異世界へと通じる扉を開く魔導士がいるとかなんとか..."));
		speeches.add(makeSpeech("武器屋には行ったかい？強い剣や貴重な矢を手に入れたいときは訪れてみるといいよ"));
	}

	@EventHandler
	public void onTalkTipsGuide(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)
				&& event.getHand().equals(EquipmentSlot.HAND)
				&& event.getRightClicked().getCustomName().equals(name)) {
			// 物知りな村人と会話する
			String speech = getRandomSpeech();
			player.sendMessage(speech);
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_WITCH_CELEBRATE, 5);

			return;
		}
	}

	private String getRandomSpeech() {
		int size = speeches.size();
		int i = (int) (Math.random() * size);

		return speeches.get(i);
	}

	private String makeSpeech(String speech) {
		speech = ChatColor.AQUA + "[" + name + "]: " + ChatColor.GOLD + speech;
		return speech;
	}
}
