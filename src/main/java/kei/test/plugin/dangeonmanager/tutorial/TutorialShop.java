package kei.test.plugin.dangeonmanager.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kei.test.plugin.items.ItemCreator;
import kei.test.plugin.items.ItemName;
import kei.test.plugin.items.ProtectorCreator;
import kei.test.plugin.items.ProtectorName;
import kei.test.plugin.items.WeaponCreator;
import kei.test.plugin.items.WeaponName;
import kei.test.plugin.monster.MonsterName;
import kei.test.plugin.sound.SoundEffect;
import net.md_5.bungee.api.ChatColor;

public class TutorialShop implements Listener {
	private final List<String> speeches = new ArrayList<>();

	private Plugin plugin;

	public TutorialShop(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;

		speeches.add(makeSpeech("よう！初めて見る顔だな！この世界に来たのは初めてか？", NPCName.TUTORIAL_NPC_2));
		speeches.add(makeSpeech("俺はここで冒険者たちに便利な武器やツールを販売してるんだ、金インゴットと交換で提供してやるぜ！", NPCName.TUTORIAL_NPC_2));
		speeches.add(
				makeSpeech("金インゴットはこの先に金鉱山があるから鉄ピッケルで採掘して入手できるぞ！", NPCName.TUTORIAL_NPC_2));
		speeches.add(makeSpeech("ん、なんだよく見ると丸腰じゃないか！そんなことだとこの先に出現する" + MonsterName.ROCK_ZOMBIE + "にやられちまうぞ！",
				NPCName.TUTORIAL_NPC_2));
		speeches.add(makeSpeech("ほら、" + WeaponName.TUTORIAL_SWORD + "をやるから頑張って倒してこい！ある程度金が集まったらここに戻ってくればアイテムと交換してやれるぞ！",
				NPCName.TUTORIAL_NPC_2));
		speeches.add(makeSpeech("金インゴットと交換できる商品はこの通りだ！しっかり準備して行け！", NPCName.TUTORIAL_NPC_2));
	}

	@EventHandler
	public void onTalkTipsGuide(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		int index1 = 0;
		if (!player.hasMetadata(TutorialMetaKey.TALK_SHOP_COUNT)) {
			player.setMetadata(TutorialMetaKey.TALK_SHOP_COUNT, new FixedMetadataValue(plugin, 0));
		} else {
			index1 = player.getMetadata(TutorialMetaKey.TALK_SHOP_COUNT).get(0).asInt();
		}

		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)
				&& event.getHand().equals(EquipmentSlot.HAND)) {

			if (event.getRightClicked().getCustomName().equals(NPCName.TUTORIAL_NPC_2)) {
				event.setCancelled(true);

				player.sendMessage(speeches.get(index1));
				if (speeches.get(index1).contains(WeaponName.TUTORIAL_SWORD)) {
					this.givePlayerTool(player);
				}

				index1++;
				if (index1 == speeches.size()) {
					this.openTutorialShop(player);
					index1 = speeches.size() - 1;
				}

				player.setMetadata(TutorialMetaKey.TALK_SHOP_COUNT, new FixedMetadataValue(plugin, index1));
				SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_VILLAGER_AMBIENT, 5);
				return;
			}

			return;
		}
	}

	// プレイヤーがチュートリアルワールドから出るときにメタデータを削除する
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();

		if (event.getFrom().getWorld().equals(Bukkit.getWorld("dangeons/tutorial"))
				&& !event.getTo().getWorld().equals(Bukkit.getWorld("dangeons/tutorial"))) {
			player.removeMetadata(TutorialMetaKey.TALK_SHOP_COUNT, plugin);
		}
	}

	// チュートリアルNPCのショップを開く
	private void openTutorialShop(Player player) {
		Merchant merchant = createTutorialMerchant();
		player.openMerchant(merchant, true);
	}

	private Merchant createTutorialMerchant() {
		List<MerchantRecipe> recipes = new ArrayList<>();
		ItemStack item;
		MerchantRecipe recipe;

		// 武器屋の剣
		item = WeaponCreator.create(WeaponName.TUTORIAL_SWORD, Material.IRON_SWORD,
				Arrays.asList("武器屋の店主が恵んでくれた鉄の剣", "若干さび付いている"), 8.0);

		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 30));
		recipes.add(recipe);

		item = ItemCreator.create(ItemName.DRY_BREAD, Material.BREAD, 10,
				Arrays.asList("パサパサに焼き固めたビスケットパン", "冒険に持っていくにはピッタリの非常食", "大事に食べよう"));
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = ProtectorCreator.createProtector(ProtectorName.IRON_HELM);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 5));
		recipes.add(recipe);

		item = ProtectorCreator.createProtector(ProtectorName.IRON_MAIL);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 10));
		recipes.add(recipe);

		item = ProtectorCreator.createProtector(ProtectorName.IRON_LEGGINGS);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 8));
		recipes.add(recipe);

		item = ProtectorCreator.createProtector(ProtectorName.IRON_BOOTS);
		recipe = new MerchantRecipe(item, 1);
		recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 5));
		recipes.add(recipe);

		// レシピをいくらでも取引できるようにする
		for (MerchantRecipe r : recipes) {
			r.setMaxUses(1000000);
		}

		Merchant merchant = Bukkit.createMerchant(NPCName.TUTORIAL_NPC_2);
		merchant.setRecipes(recipes);

		return merchant;
	}

	private String makeSpeech(String speech, String name) {
		speech = ChatColor.AQUA + "[" + name + "]: " + ChatColor.GOLD + speech;
		return speech;
	}

	// プレイヤーにツールを与える
	private void givePlayerTool(Player player) {
		ItemStack item = WeaponCreator.create(WeaponName.TUTORIAL_SWORD, Material.IRON_SWORD,
				Arrays.asList("武器屋の店主が恵んでくれた鉄の剣", "若干さび付いている"), 8.0);

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
			if (name.equals(WeaponName.TUTORIAL_SWORD)) {
				player.getInventory().remove(content);
				break;
			}
		}

		player.getInventory().addItem(item);

		SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_PLAYER_LEVELUP, 5);
		SoundEffect.getInstance().playSoundEffect(player, Sound.ENTITY_ITEM_PICKUP, 5);
	}
}
