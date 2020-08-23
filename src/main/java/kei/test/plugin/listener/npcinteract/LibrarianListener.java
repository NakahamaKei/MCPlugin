package kei.test.plugin.listener.npcinteract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class LibrarianListener implements Listener {

	private final String name = ChatColor.AQUA + "熟練の魔導士";

	private final List<String> speeches = new ArrayList<>();

	private Merchant merchant;

	public LibrarianListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		speeches.add(makeSpeech("ホッホッホ、今日は何を買い求めているのかね？"));
		speeches.add(makeSpeech("うちの魔力書の性能は折り紙付きよ、さあどんどん買っていっておくれ"));
		speeches.add(makeSpeech("「見習い魔導士」の店には行ったかい？あそこの地下には私の相方が住んでおるよ"));

		this.merchant = getMerchant();
	}

	@EventHandler
	public void onRightClickLibrarian(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)
				&& event.getHand().equals(EquipmentSlot.HAND)
				&& event.getRightClicked().getCustomName().equals(name)) {
			// 醸造士と会話する
			String speech = getRandomSpeech();
			player.sendMessage(speech);
			SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_WITCH_CELEBRATE, 5);

			player.openMerchant(this.merchant, true);
		}
	}

	private Merchant getMerchant() {
		List<MerchantRecipe> recipes = new ArrayList<>();
		ItemStack item;
		MerchantRecipe recipe;
		EnchantmentStorageMeta meta;

		item = createNewItem("剛腕の魔導書１", Material.ENCHANTED_BOOK, Arrays.asList("少し力強さを感じる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.DAMAGE_ALL, 3, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("剛腕の魔導書２", Material.ENCHANTED_BOOK, Arrays.asList("力強さを感じる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.DAMAGE_ALL, 4, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 20));
		recipes.add(recipe);

		item = createNewItem("剛腕の魔導書３", Material.ENCHANTED_BOOK, Arrays.asList("かなり力強さを感じる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.DAMAGE_ALL, 5, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 30));
		recipe.addIngredient(new ItemStack(Material.DIAMOND, 3));
		recipes.add(recipe);

		item = createNewItem("剛力の魔導書１", Material.ENCHANTED_BOOK, Arrays.asList("少し力強さを感じる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.ARROW_DAMAGE, 3, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("剛力の魔導書２", Material.ENCHANTED_BOOK, Arrays.asList("力強さを感じる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.ARROW_DAMAGE, 4, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 20));
		recipes.add(recipe);

		item = createNewItem("剛力の魔導書３", Material.ENCHANTED_BOOK, Arrays.asList("かなり力強さを感じる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.ARROW_DAMAGE, 5, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 30));
		recipe.addIngredient(new ItemStack(Material.DIAMOND, 3));
		recipes.add(recipe);

		item = createNewItem("修繕の魔導書", Material.ENCHANTED_BOOK, Arrays.asList("道具や武器と合成することで", "修繕能力を得ることができる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.MENDING, 1, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 25));
		recipes.add(recipe);

		item = createNewItem("火矢の魔導書", Material.ENCHANTED_BOOK, Arrays.asList("弓と合成することで火矢を放つ", "ことができる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.ARROW_FIRE, 2, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 15));
		recipes.add(recipe);

		item = createNewItem("火の魔導書", Material.ENCHANTED_BOOK, Arrays.asList("剣と合成することで火の剣を", "作ることができる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.FIRE_ASPECT, 1, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 5));
		recipes.add(recipe);

		item = createNewItem("業火の魔導書", Material.ENCHANTED_BOOK, Arrays.asList("剣と合成することで業火の剣を", "作ることができる魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.FIRE_ASPECT, 2, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 15));
		recipes.add(recipe);

		item = createNewItem("耐衝撃の魔導書１", Material.ENCHANTED_BOOK, Arrays.asList("防具と合成することで", "鉄のごとき防御力を得る魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.PROTECTION_ENVIRONMENTAL, 3, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = createNewItem("耐衝撃の魔導書２", Material.ENCHANTED_BOOK, Arrays.asList("防具と合成することで", "鉄のごとき防御力を得る魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.PROTECTION_ENVIRONMENTAL, 4, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 15));
		recipes.add(recipe);

		item = createNewItem("採掘士の魔導書", Material.ENCHANTED_BOOK, Arrays.asList("ツールと合成することで", "驚異的な速度を得る魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.DIG_SPEED, 5, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 20));
		recipes.add(recipe);

		item = createNewItem("超耐久の魔導書", Material.ENCHANTED_BOOK, Arrays.asList("ツールと合成することで", "驚異的な硬度を得る魔導書"));
		meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(EnchantmentWrapper.DURABILITY, 3, true);
		item.setItemMeta(meta);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 20));
		recipes.add(recipe);

		// レシピをいくらでも取引できるようにする
		for (MerchantRecipe r : recipes) {
			r.setMaxUses(1000000);
		}

		Merchant merchant = Bukkit.createMerchant(name);
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
		speech = ChatColor.AQUA + "[" + name + "]: " + ChatColor.GOLD + speech;
		return speech;
	}
}
