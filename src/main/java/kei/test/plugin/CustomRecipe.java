package kei.test.plugin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class CustomRecipe {
	private Plugin plugin;

	public CustomRecipe(Plugin plugin) {
		this.plugin = plugin;

		makePortalRecipe(); // ポータルレシピを追加

		List<String> itemLores = Arrays.asList("匠にビフォーアフターされた者", "たちの怨念が詰まった剣",
				"匠を一撃で仕留めることができる");
		createMetuTakumi("滅・匠の剣", Material.IRON_SWORD, itemLores, "滅・匠の魂", Material.CREEPER_HEAD, "metuTakumi");
	}

	private void makePortalRecipe() {
		ItemStack item = new ItemStack(Material.CRYING_OBSIDIAN);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.GREEN + "ポータル");
		List<String> lores = Arrays.asList("ふしぎな引力で引き合う\n", "ポータルを設置できる\n",
				"ポータルを二つ置いて\n", "上でスニークして移動する\n", "右クリックで破壊可能\n");
		meta.setLore(lores);

		item.setItemMeta(meta);

		// レシピ作成
		ItemStack cobbleStone = new ItemStack(Material.OBSIDIAN);
		ItemMeta cobbleStoneMeta = cobbleStone.getItemMeta();
		cobbleStoneMeta.setDisplayName(ChatColor.GREEN + "ポータルのかけら");
		//		lores = Arrays.asList(ChatColor.WHITE + "ポータルの材料\n", ChatColor.WHITE + "ポータルのかけらを作業台に",
		//				ChatColor.WHITE + "9個並べてクラフトすると", "ポータルを作成することができる",
		//				ChatColor.WHITE + "置くと黒曜石にもどるので注意");
		//		cobbleStone.setLore(lores);
		cobbleStone.setItemMeta(cobbleStoneMeta);

		NamespacedKey key = new NamespacedKey(plugin, "portal");
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		recipe.shape("SSS", "SSS", "SSS");
		recipe.setIngredient('S', cobbleStone);

		Bukkit.addRecipe(recipe);
	}

	private void createMetuTakumi(String name, Material material, List<String> itemLores, String ingName,
			Material ingMaterial, String key) {
		ItemStack item = createItem(name, material, itemLores);
		ItemStack ing = createIngredients(ingName, ingMaterial);
		ShapedRecipe recipe = createRecipe(item, ing, key);

		recipe.shape(" I ", " S ", "   ");
		recipe.setIngredient('I', ing);
		recipe.setIngredient('S', new ItemStack(Material.IRON_SWORD));

		Bukkit.addRecipe(recipe);
	}

	private ItemStack createItem(String name, Material material, List<String> lores) {
		ItemStack item = new ItemStack(material);

		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.GREEN + name);

		for (int i = 0; i < lores.size(); i++) {
			String tmp = ChatColor.WHITE + lores.get(i);
			lores.set(i, tmp);
		}

		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack createIngredients(String name, Material material) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + name);
		item.setItemMeta(meta);
		return item;
	}

	private ShapedRecipe createRecipe(ItemStack item, ItemStack ingredients, String key) {
		NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
		ShapedRecipe recipe = new ShapedRecipe(namespacedKey, item);
		return recipe;
	}
}
