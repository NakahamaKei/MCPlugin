package kei.test.plugin.listener.npcinteract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.items.ItemCreator;
import kei.test.plugin.items.ItemName;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class TradeListener implements Listener {

	private String wonderMerchantName = ChatColor.AQUA + "不思議な商人";
	private String weaponerName = ChatColor.AQUA + "武器屋のおっさん";

	private final List<String> wonderMerchantSpeeches = new ArrayList<>();
	private final List<String> weaponerSpeeches = new ArrayList<>();

	private final Merchant wonderMerchant;
	private final Merchant weaponerMerchant;

	public TradeListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		// 不思議な商人のセリフを追加
		wonderMerchantSpeeches.add(makeSpeech("ポータルのかけらは、9個集めてクラフトするとポータルを作成できるのさ", wonderMerchantName));
		wonderMerchantSpeeches.add(makeSpeech("ポータルは異なるワールド間でも移動することができるよ", wonderMerchantName));
		wonderMerchantSpeeches.add(makeSpeech("ポータルは一人一つまでしか作れないよ", wonderMerchantName));

		// 武器屋のおっさんのセリフを追加
		weaponerSpeeches.add(makeSpeech("そんな装備で大丈夫か？うちでいい武器を揃えていきな！！", weaponerName));
		weaponerSpeeches.add(makeSpeech("最近大量にモンスターが沸くようになっちまった...用心するこったな", weaponerName));
		weaponerSpeeches.add(makeSpeech("金はゾンビピグリンを倒すとたまに手に入るぞ、ガンガン狩ってうちで装備をアップグレードしていってくれい！", weaponerName));

		this.wonderMerchant = getWonderMerchant();
		this.weaponerMerchant = getWeaponMerchant();
	}

	@EventHandler
	public void onTrade(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked() instanceof Villager) {
			Entity entity = event.getRightClicked();

			if (entity.getCustomName().equals(wonderMerchantName)) {
				// 不思議な商人と会話する
				String speech = getRandomSpeech(wonderMerchantSpeeches);
				player.sendMessage(speech);
				SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_VILLAGER_CELEBRATE, 5);

				// 不思議な商人ショップ画面を開く
				player.openMerchant(this.wonderMerchant, true);
			}

			if (entity.getCustomName().equals(weaponerName)) {
				// 武器屋のおっさんと会話する
				String speech = getRandomSpeech(weaponerSpeeches);
				player.sendMessage(speech);
				SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_VILLAGER_CELEBRATE, 5);

				// 武器屋ショップ画面を開く
				player.openMerchant(this.weaponerMerchant, true);
			}
		}
	}

	private Merchant getWonderMerchant() {
		ItemStack item = createNewItem("ポータルのかけら", Material.OBSIDIAN, null);

		ItemStack item2 = createNewItem("滅・匠の魂", Material.CREEPER_HEAD, null);

		ItemStack item3 = ItemCreator.create(ItemName.CHEST_KEY, Material.TRIPWIRE_HOOK, 1,
				Arrays.asList("チェストにロックをかけることができる鍵", "右クリックで鍵をかけるこができる", "左クリックでロックの有無と所有者の確認ができる"));

		//		ItemStack item4 = Item.createSortStick();

		MerchantRecipe recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.COBBLESTONE, 32));
		MerchantRecipe recipe2 = new MerchantRecipe(item2, 1);
		recipe2.addIngredient(new ItemStack(Material.GOLD_INGOT, 50));
		MerchantRecipe recipe3 = new MerchantRecipe(item3, 1);
		recipe3.addIngredient(new ItemStack(Material.GOLD_INGOT, 30));
		recipe3.addIngredient(new ItemStack(Material.LAPIS_BLOCK, 30));
		//		MerchantRecipe recipe4 = new MerchantRecipe(item4, 1);
		//		recipe4.addIngredient(new ItemStack(Material.GOLD_INGOT, 20));
		//		recipe4.addIngredient(new ItemStack(Material.LAPIS_BLOCK, 40));

		Merchant merchant = Bukkit.createMerchant("不思議な行商人");
		List<MerchantRecipe> recipes = new ArrayList<>();
		recipes.add(recipe);
		recipes.add(recipe2);
		recipes.add(recipe3);
		//		recipes.add(recipe4);

		// レシピをいくらでも取引できるようにする
		for (MerchantRecipe r : recipes) {
			r.setMaxUses(1000000);
		}

		merchant.setRecipes(recipes);
		return merchant;
	}

	// 武器屋のおっさん
	private Merchant getWeaponMerchant() {
		List<MerchantRecipe> recipes = new ArrayList<>();
		ItemStack item;
		AttributeModifier modifier;
		MerchantRecipe recipe;
		ItemMeta meta;

		// 木刀
		item = createNewItem("木刀", Material.WOODEN_SWORD, Arrays.asList("木製の剣\n", "ないよりはマシ\n", "気合の限りぶん回せ！"));
		item.addEnchantment(EnchantmentWrapper.DAMAGE_ALL, 5);
		item.addEnchantment(EnchantmentWrapper.SWEEPING_EDGE, 2);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 5));
		recipes.add(recipe);

		// 亡者の太刀
		modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 9.0,
				Operation.ADD_NUMBER, EquipmentSlot.HAND);
		item = createNewItem("亡者の太刀", Material.IRON_SWORD, Arrays.asList("亡者の怨念が籠った太刀\n", "彷徨う亡者を黄泉送りにする"));
		item.addEnchantment(EnchantmentWrapper.DAMAGE_UNDEAD, 4);
		meta = item.getItemMeta();
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
		item.setItemMeta(meta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 30));
		recipes.add(recipe);

		// 渾身の一撃
		modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 50.0,
				Operation.ADD_NUMBER, EquipmentSlot.HAND);
		item = createNewItem("渾身の一撃", Material.IRON_SWORD,
				Arrays.asList("最強の一撃を繰り出す剣", "ただし使用者は大きな反動ダメージを受ける"));
		item.addEnchantment(EnchantmentWrapper.SWEEPING_EDGE, 3);
		meta = item.getItemMeta();
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
		item.setItemMeta(meta);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.DIAMOND, 15));
		recipes.add(recipe);

		// 弓
		item = createNewItem("弓", Material.BOW,
				Arrays.asList("ごく一般的な木製の弓"));
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 5));
		recipes.add(recipe);

		// 矢
		item = new ItemStack(Material.ARROW, 15);
		recipe = new MerchantRecipe(item, 20);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 3));
		recipes.add(recipe);

		// ボウガン
		item = createNewItem("クロスボウ", Material.CROSSBOW,
				Arrays.asList("ごく一般的なクロスボウ"));
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 5));
		recipes.add(recipe);

		// トライデント
		item = new ItemStack(Material.TRIDENT);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 20));
		recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 10));
		recipes.add(recipe);

		// ミニボトル
		item = createNewItem("ミニボトル", Material.EXPERIENCE_BOTTLE,
				Arrays.asList("レベル20相当のExpが手に入るボトル"));
		recipe = new MerchantRecipe(item, 1);
		ItemStack miniBottle = item.clone();
		recipe.addIngredient(new ItemStack(Material.LAPIS_LAZULI, 50));
		recipes.add(recipe);

		// ミディアムボトル
		item = createNewItem("ミディアムボトル", Material.EXPERIENCE_BOTTLE,
				Arrays.asList("レベル30相当のExpが手に入るボトル"));
		recipe = new MerchantRecipe(item, 1);
		ItemStack mediumBottle = item.clone();
		miniBottle.setAmount(2);
		recipe.addIngredient(miniBottle);
		recipe.addIngredient(new ItemStack(Material.LAPIS_LAZULI, 20));
		recipes.add(recipe);

		// ラージボトル
		item = createNewItem("ラージボトル", Material.EXPERIENCE_BOTTLE,
				Arrays.asList("レベル50相当のExpが手に入るボトル"));
		recipe = new MerchantRecipe(item, 1);
		ItemStack largeBottle = item.clone();
		mediumBottle.setAmount(9);
		recipe.addIngredient(mediumBottle);
		recipe.addIngredient(new ItemStack(Material.LAPIS_LAZULI, 20));
		recipes.add(recipe);

		//		// タイタンボトル
		//		item = createNewItem("タイタンボトル", Material.EXPERIENCE_BOTTLE,
		//				Arrays.asList("レベル68相当のExpが手に入るボトル"));
		//		recipe = new MerchantRecipe(item, 1);
		//		//		ItemStack titanBottle = item.clone();
		//		largeBottle.setAmount(20);
		//		recipe.addIngredient(largeBottle);
		//		recipe.addIngredient(new ItemStack(Material.LAPIS_LAZULI, 20));
		//		recipes.add(recipe);

		// レシピをいくらでも取引できるようにする
		for (MerchantRecipe r : recipes) {
			r.setMaxUses(1000000);
		}

		Merchant merchant = Bukkit.createMerchant(weaponerName);
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

	private String makeSpeech(String speech, String name) {
		speech = ChatColor.AQUA + "[" + name + "]: " + ChatColor.GOLD + speech;
		return speech;
	}

	private String getRandomSpeech(List<String> speeches) {
		int size = speeches.size();
		int i = (int) (Math.random() * size);

		return speeches.get(i);
	}

}
