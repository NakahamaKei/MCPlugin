package kei.test.plugin.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import kei.test.plugin.KillCounter;
import kei.test.plugin.PlayerManager;
import kei.test.plugin.listener.KillListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MyPluginCommandExecutor implements CommandExecutor {

	private List<BukkitTask> taskList = new ArrayList<>();

	public MyPluginCommandExecutor(Plugin plugin) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
			String worldName = player.getWorld().getName();

			// プレイヤーのステータスを表示
			if (command.getName().equalsIgnoreCase("stats")) {
				//				UUID uuid = player.getUniqueId();
				//				PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);

				//				String msg = "============Kill Count============\n";
				//				msg += "Zombie：" + playerInfo.getKillCount("zombie") + "\n";
				//				msg += "Skeleton：" + playerInfo.getKillCount("skeleton") + "\n";
				//				msg += "Creeper：" + playerInfo.getKillCount("creeper") + "\n";
				//				msg += "Spider：" + playerInfo.getKillCount("spider") + "\n";
				//				msg += "Slime：" + playerInfo.getKillCount("slime") + "\n";
				//				msg += "Enderman：" + playerInfo.getKillCount("enderman") + "\n";
				//				msg += "Blaze：" + playerInfo.getKillCount("blaze") + "\n";
				//				msg += "Ghast：" + playerInfo.getKillCount("ghast") + "\n";
				//				msg += "==================================\n";

				Inventory inventory = Bukkit.createInventory(null, 27, "討伐ステータス");
				KillCounter killCounter = new KillCounter(player.getUniqueId());
				inventory.setItem(0, killCounter.getItemStack("zombie"));
				inventory.setItem(1, killCounter.getItemStack("skeleton"));
				inventory.setItem(2, killCounter.getItemStack("creeper"));
				inventory.setItem(3, killCounter.getItemStack("spider"));
				inventory.setItem(4, killCounter.getItemStack("slime"));
				inventory.setItem(5, killCounter.getItemStack("enderman"));
				inventory.setItem(6, killCounter.getItemStack("blaze"));
				inventory.setItem(7, killCounter.getItemStack("ghast"));
				player.openInventory(inventory);

				//				CustomVillager customVillager = new CustomVillager(player.getLocation());
				//				WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
				//				world.addEntity(customVillager);

				return true;
			}

			if (command.getName().equalsIgnoreCase("join")) {
				if (args.length != 1) {
					sender.sendMessage("遷移先のワールド名を指定してください");
					return false;
				}

				if (Bukkit.getWorld(args[0]) == null) {
					sender.sendMessage(args[0] + "という名前のワールドは存在しません");
					return false;
				}

				if (PlayerManager.getInstance().exist(uuid)) {
					if (worldName.equals("world")) {
						PlayerManager.getInstance().setPlayerHomeLoc(player.getUniqueId(), player.getLocation());
					}
				}

				if (worldName.equals(args[0])) {
					sender.sendMessage(worldName + "は現在いるワールドです");
				} else {
					Location loc = Bukkit.getWorld(args[0]).getSpawnLocation();
					player.teleport(loc);
				}

				return false;
			}

			if (command.getName().equalsIgnoreCase("home")) {
				if (args.length != 0) {
					return false;
				}

				if (!player.getWorld().getName().equalsIgnoreCase("world")) {
					player.sendMessage("不思議な力でテレポートを阻止された");
					return true;
				}

				List<Entity> entities = player.getNearbyEntities(10D, 10D, 10D);
				boolean exist = false;
				for (Entity en : entities) {
					if (en instanceof Monster) {
						exist = true;
						break;
					}
				}

				if (exist) {
					taskList.add(new BukkitRunnable() {
						private int count = 0;

						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
									new TextComponent("近くに敵モンスターがいるためテレポートできません"));

							if (taskList.size() > 1) {
								taskList.get(0).cancel();
								taskList.remove(0);
							}
							count++;

							if (count >= 5) {
								this.cancel();
							}
						}
					}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0L, 20L));
				} else {
					Location loc = player.getBedSpawnLocation();
					if (loc != null) {
						player.sendMessage("ベッドまでテレポートしました");
						player.teleport(loc);
						player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
					} else {
						player.sendMessage("テレポート先に登録されているベッドはありません");
					}
				}

				return true;
			}

			if (command.getName().equalsIgnoreCase("init")) {

				if (args.length != 0) {
					return false;
				}

				if (!player.getWorld().getName().equalsIgnoreCase("world")) {
					player.sendMessage("不思議な力でテレポートを阻止された");
					return true;
				}

				List<Entity> entities = player.getNearbyEntities(10D, 10D, 10D);
				boolean exist = false;
				for (Entity en : entities) {
					if (en instanceof Monster) {
						exist = true;
						break;
					}
				}

				if (exist) {
					taskList.add(new BukkitRunnable() {
						private int count = 0;

						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
									new TextComponent("近くに敵モンスターがいるためテレポートできません"));

							if (taskList.size() > 1) {
								taskList.get(0).cancel();
								taskList.remove(0);
							}
							count++;

							if (count >= 5) {
								this.cancel();
							}
						}
					}.runTaskTimer(JavaPlugin.getProvidingPlugin(KillListener.class), 0L, 20L));
				} else {
					Location loc = Bukkit.getWorld("world").getSpawnLocation();
					if (loc != null) {
						player.teleport(Bukkit.getWorld("world").getSpawnLocation());
						player.sendMessage("初期リスポーン地点に移動しました");
						player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
					} else {
						player.sendMessage("ワールドスポーン地点の取得に失敗しました");
					}
				}

				return true;
			}

			if (command.getName().equalsIgnoreCase("command")) {
				sender.sendMessage(ChatColor.WHITE + "======================================");
				sender.sendMessage(ChatColor.GREEN + "/stats: " + ChatColor.WHITE + "モブの討伐数を表示");
				sender.sendMessage(ChatColor.GREEN + "/command: " + ChatColor.WHITE + "利用可能なコマンドリストを表示");
				sender.sendMessage(ChatColor.GREEN + "/newinfo: " + ChatColor.WHITE + "最新機能やニュース一覧を表示");
				sender.sendMessage(ChatColor.GREEN + "/init: " + ChatColor.WHITE + "初期リスポーン地点にテレポート");
				sender.sendMessage(ChatColor.GREEN + "/home: " + ChatColor.WHITE + "最後に登録したベッドの位置にテレポート");
				sender.sendMessage(ChatColor.GREEN + "/join resource: " + ChatColor.WHITE + "資源ワールドへテレポート");
				sender.sendMessage(ChatColor.GREEN + "/join world: " + ChatColor.WHITE + "メインワールドへテレポート");
				sender.sendMessage(ChatColor.WHITE + "======================================");
				return true;
			}

			if (command.getName().equalsIgnoreCase("newinfo")) {
				sender.sendMessage(ChatColor.WHITE + "*******************最新情報一覧*******************");
				sender.sendMessage(ChatColor.GREEN + "「モンスタートゥループ」追加：");
				sender.sendMessage(ChatColor.GOLD + "  -> ワールド内のモンスターが集団でスポーンするようになりました！");
				sender.sendMessage(ChatColor.GREEN + "「強化モンスター」追加：");
				sender.sendMessage(ChatColor.GOLD + "  -> ワールド内のモンスターが強化されて出現するようになりました！");
				sender.sendMessage(ChatColor.GREEN + "「墓荒らし機能」追加：");
				sender.sendMessage(ChatColor.GOLD + "  -> もう死んでも大丈夫！その場でチェストが生成されてアイテムが保管されます！");
				sender.sendMessage(ChatColor.WHITE + "*******************最新情報一覧*******************");
				return true;
			}

			if (command.getName().equalsIgnoreCase("tpblock")) {
				if (args.length != 0) {
					return false;
				}

				player.sendMessage("TPブロックを付与しました");
				ItemStack is = new ItemStack(Material.CRYING_OBSIDIAN);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("ポータル");
				List<String> lores = Arrays.asList("ふしぎな引力で引き合う", "地点同士を結ぶポータルを", "作成する");
				im.setLore(lores);

				is.setItemMeta(im);

				Inventory inv = player.getInventory();
				if (inv.firstEmpty() == -1) {
					player.sendMessage("空きスロットがないため、ポータルを受け取ることができませんでした");
				} else {
					player.getInventory().addItem(is);
				}

				Merchant merchant = getWonderMerchant();
				player.openMerchant(merchant, true);
				return true;
			}

			if (command.getName().equalsIgnoreCase("killMonsters")) {
				if (args.length != 0) {
					return false;
				}

				List<Entity> entities = player.getNearbyEntities(20.0, 20.0, 20.0);
				for (Entity entity : entities) {
					if (!(entity instanceof LivingEntity) || !(entity instanceof Monster))
						continue;

					Monster mons = ((Monster) entity);
					mons.remove();

				}
				return true;
			}
		}

		// 新しいワールドを生成
		if (command.getName().equalsIgnoreCase("create")) {

			if (args.length != 1) {
				sender.sendMessage("作成するワールド名を指定してください");
				return false;
			}

			if (Bukkit.getWorld(args[0]) == null) {
				WorldCreator wc = new WorldCreator(args[0]);

				wc.environment(World.Environment.THE_END);
				wc.type(WorldType.NORMAL);

				if (wc.createWorld() == null) {
					sender.sendMessage(args[0] + "ワールドを作成しました");
					return true;
				} else {
					sender.sendMessage(args[0] + "ワールドの作成に失敗しました");
					return false;
				}
			} else {
				sender.sendMessage(args[0] + "はすでに存在するワールド名です");
			}

			return false;
		}

		//		if (command.getName().equalsIgnoreCase("unload")) {
		//			World world = Bukkit.getWorld("resource");
		//			if (!world.equals(null)) {
		//				Bukkit.getServer().unloadWorld(world, true);
		//			}
		//		}

		return false;
	}

	private Merchant getWonderMerchant() {
		ItemStack item = new ItemStack(Material.OBSIDIAN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "ポータル");
		List<String> lores = Arrays.asList(ChatColor.WHITE + "ふしぎな引力で引き合う\n", ChatColor.WHITE + "ポータルを設置できる\n",
				ChatColor.WHITE + "ポータルを二つ置いて\n", ChatColor.WHITE + "上でスニークして移動する\n", ChatColor.WHITE + "右クリックで破壊可能\n");
		meta.setLore(lores);
		item.setItemMeta(meta);

		MerchantRecipe recipe = new MerchantRecipe(item, 10);
		recipe.addIngredient(new ItemStack(Material.COBBLESTONE, 64));

		MerchantRecipe recipe2 = new MerchantRecipe(new ItemStack(Material.END_CRYSTAL), 1);
		recipe2.addIngredient(item);

		Merchant merchant = Bukkit.createMerchant("不思議な行商人");
		List<MerchantRecipe> recipes = new ArrayList<>();
		recipes.add(recipe);
		recipes.add(recipe2);

		merchant.setRecipes(recipes);
		return merchant;
	}

}
