package kei.test.plugin.command;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

// 交易関係のモブ召喚プラグイン
public class SummonTraderCommandExecutor implements CommandExecutor {

	public SummonTraderCommandExecutor(Plugin plugin) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
			String worldName = player.getWorld().getName();

			if (command.getName().equalsIgnoreCase("trader")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setAdult();
				v.setAI(false);
				v.setInvulnerable(true);
				v.setCustomName(ChatColor.AQUA + "不思議な商人");
				v.setAware(true);
				return true;
			}

			// 武器屋のおっさんを召喚する
			if (command.getName().equalsIgnoreCase("weaponer")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setAdult();
				v.setAI(false);
				v.setInvulnerable(true);
				v.setCustomName(ChatColor.AQUA + "武器屋のおっさん");
				v.setAware(true);
				return true;
			}

			// 見習い魔導士を召喚する
			if (command.getName().equalsIgnoreCase("mage")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setAdult();
				v.setAI(false);
				v.setInvulnerable(true);
				v.setCustomName(ChatColor.AQUA + "見習い魔導士");
				v.setAware(true);
				return true;
			}

			// 熟練の魔導士を召喚する
			if (command.getName().equalsIgnoreCase("promage")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setAdult();
				v.setAI(false);
				v.setInvulnerable(true);
				v.setCustomName(ChatColor.AQUA + "熟練の魔導士");
				v.setAware(true);
				return true;
			}

			// ダンジョンセレクターを召喚する
			if (command.getName().equalsIgnoreCase("guide")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setAdult();
				v.setAI(false);
				v.setInvulnerable(true);
				v.setCustomName(ChatColor.AQUA + "隻眼の魔導士");
				v.setAware(true);

				return true;
			}

			// 物知りな村人を召喚する
			if (command.getName().equalsIgnoreCase("tipsguide")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setAdult();
				v.setAI(false);
				v.setInvulnerable(true);
				v.setCustomName(ChatColor.AQUA + "物知りな村人");
				v.setAware(true);

				return true;
			}

			// 司書を召喚する
			if (command.getName().equalsIgnoreCase("library")) {
				if (args.length != 0) {
					return false;
				}

				Location curLoc = player.getLocation();
				Villager v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.SILK_TOUCH, 1, true);
				item.setItemMeta(meta);

				MerchantRecipe recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.DAMAGE_ALL, 5, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.PROTECTION_FALL, 5, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(1, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.WATER_WORKER, 1, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.SWEEPING_EDGE, 3, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.ARROW_DAMAGE, 5, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.ARROW_FIRE, 2, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(1, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.KNOCKBACK, 2, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(1, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.MENDING, 1, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.OXYGEN, 3, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(1, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.DAMAGE_UNDEAD, 5, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				item = new ItemStack(Material.GLASS);

				recipe = new MerchantRecipe(item, 4);
				recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(1, recipe);

				//////////////////////////
				v = (Villager) player.getWorld().spawnEntity(curLoc, EntityType.VILLAGER);
				v.setVillagerType(Type.SNOW);
				v.setProfession(Profession.LIBRARIAN);
				v.setAdult();
				v.setVillagerLevel(4);
				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.LOYALTY, 3, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(0, recipe);

				item = new ItemStack(Material.ENCHANTED_BOOK);
				meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(Enchantment.RIPTIDE, 3, true);
				item.setItemMeta(meta);

				recipe = new MerchantRecipe(item, 1);
				recipe.addIngredient(new ItemStack(Material.EMERALD, getRandom()));
				recipe.addIngredient(new ItemStack(Material.BOOK, 1));
				recipe.setMaxUses(10000000);
				v.setRecipe(1, recipe);

				return true;
			}
		}

		return false;
	}

	private int getRandom() {
		int rand = (int) (Math.random() * 10) + 7;
		return rand;
	}
}
