package kei.test.plugin.listener.npcinteract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectTypeWrapper;

import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class BrewerListener implements Listener {
	private final String brewerName = ChatColor.AQUA + "見習い魔導士";

	private final List<String> speeches = new ArrayList<>();

	private final Merchant merchant;

	public BrewerListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		// 魔導士のセリフを追加
		speeches.add(makeSpeech("やぁ、僕は見習い魔導士！今日は何を買いに来たのかな"));
		speeches.add(makeSpeech("外は怖いモンスターでいっぱいだよ、回復アイテムを買っていってね！"));
		speeches.add(makeSpeech("アロエヨーグルトは傷に良く効くよ！ぜひ買っていってね"));

		this.merchant = getBrewerMerchant();
	}

	@EventHandler
	public void onRightClickBrewer(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)
				&& event.getHand().equals(EquipmentSlot.HAND)
				&& event.getRightClicked().getCustomName().equals(brewerName)) {
			// 醸造士と会話する
			String speech = getRandomSpeech();
			player.sendMessage(speech);
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_WITCH_CELEBRATE, 5);

			Entity entity = event.getRightClicked();

			// 醸造士を作成する
			if (entity.getCustomName().equals(brewerName)) {
				player.openMerchant(this.merchant, true);
			}
		}
	}

	// 醸造士を作成する
	private Merchant getBrewerMerchant() {
		List<MerchantRecipe> recipes = new ArrayList<>();
		ItemStack item;
		AttributeModifier modifier;
		MerchantRecipe recipe;
		ItemMeta meta;
		PotionMeta pmeta;

		item = createNewItem("アロエヨーグルト", Material.POTION, Arrays.asList("アロエがたっぷり入ったヨーグルト", "傷を素早く治癒し、更に一時的に再生能力を得る"));
		pmeta = (PotionMeta) item.getItemMeta();
		//		pmeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, false));
		pmeta.addCustomEffect(new PotionEffect(PotionEffectTypeWrapper.HEAL, 10, 2, true), true);
		pmeta.addCustomEffect(new PotionEffect(PotionEffectTypeWrapper.REGENERATION, 20 * 10, 4, true), true);
		//		modifier = new AttributeModifier(UUID.randomUUID(), )

		item.setItemMeta(pmeta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("ピッグマンエナジー", Material.POTION,
				Arrays.asList("しつこいアイツの生命力を", "しっかり詰め込んだエナドリ", "攻撃速度と採掘速度を一時的に上昇させる"));
		pmeta = (PotionMeta) item.getItemMeta();
		pmeta.addCustomEffect(new PotionEffect(PotionEffectTypeWrapper.FAST_DIGGING, 20 * 600, 2, true), true);
		item.setItemMeta(pmeta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("暗視ドリンク", Material.POTION,
				Arrays.asList("暗い場所でも明るく見えるようになる薬", "洞窟や夜の探検に役立てよう"));
		pmeta = (PotionMeta) item.getItemMeta();
		pmeta.addCustomEffect(new PotionEffect(PotionEffectTypeWrapper.NIGHT_VISION, 20 * 600, 1, true), true);
		item.setItemMeta(pmeta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("硬化ドリンク", Material.POTION,
				Arrays.asList("一時的に身体を硬化する薬", "防御力を向上させる"));
		pmeta = (PotionMeta) item.getItemMeta();
		pmeta.addCustomEffect(new PotionEffect(PotionEffectTypeWrapper.DAMAGE_RESISTANCE, 20 * 300, 1, true), true);
		item.setItemMeta(pmeta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("冷却ドリンク", Material.POTION,
				Arrays.asList("一時的に身体を冷却する薬", "火耐性を強化する"));
		pmeta = (PotionMeta) item.getItemMeta();
		pmeta.addCustomEffect(new PotionEffect(PotionEffectTypeWrapper.FIRE_RESISTANCE, 20 * 300, 1, true), true);
		item.setItemMeta(pmeta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		// レシピをいくらでも取引できるようにする
		for(MerchantRecipe r : recipes) {
			r.setMaxUses(1000000);
		}

		Merchant merchant = Bukkit.createMerchant(brewerName);
		merchant.setRecipes(recipes);

		return merchant;
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

	private String getRandomSpeech() {
		int size = speeches.size();
		int i = (int) (Math.random() * size);

		return speeches.get(i);
	}

	private String makeSpeech(String speech) {
		speech = ChatColor.AQUA + "[" + brewerName + "]: " + ChatColor.GOLD + speech;
		return speech;
	}

}
