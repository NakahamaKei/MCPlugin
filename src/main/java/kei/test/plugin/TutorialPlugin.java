package kei.test.plugin;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import kei.test.plugin.chestlock.ChestLock;
import kei.test.plugin.chestlock.ChestLockDB;
import kei.test.plugin.chestlock.ChestLockListener;
import kei.test.plugin.command.MyPluginCommandExecutor;
import kei.test.plugin.command.SummonTraderCommandExecutor;
import kei.test.plugin.dangeonmanager.StrangeCaveManager;
import kei.test.plugin.dangeonmanager.tutorial.TutorialDangeonManager;
import kei.test.plugin.dangeonmanager.tutorial.TutorialNPC;
import kei.test.plugin.dangeonmanager.tutorial.TutorialNPCSummonCommand;
import kei.test.plugin.dangeonmanager.tutorial.TutorialShop;
import kei.test.plugin.database.NameTagMobsDB;
import kei.test.plugin.itemsort.ItemSortListener;
import kei.test.plugin.listener.BedListener;
import kei.test.plugin.listener.ChatListener;
import kei.test.plugin.listener.GetExpBottleListener;
import kei.test.plugin.listener.InventoryListener;
import kei.test.plugin.listener.KillListener;
import kei.test.plugin.listener.LoginListener;
import kei.test.plugin.listener.NameTagListener;
import kei.test.plugin.listener.PlayerBehaviorListener;
import kei.test.plugin.listener.PlayerDamagedListener;
import kei.test.plugin.listener.PlayerDeathListener;
import kei.test.plugin.listener.PlayerRangeListener;
import kei.test.plugin.listener.anvil.AnvilNameTagListener;
import kei.test.plugin.listener.mobspawn.CreeperSpawnListener;
import kei.test.plugin.listener.mobspawn.MobSpawnListener;
import kei.test.plugin.listener.mobspawn.VindicatorSpawnListener;
import kei.test.plugin.listener.mobspawn.ZPigSpawnListener;
import kei.test.plugin.listener.npcinteract.BrewerListener;
import kei.test.plugin.listener.npcinteract.GuideListener;
import kei.test.plugin.listener.npcinteract.LibrarianListener;
import kei.test.plugin.listener.npcinteract.TipsGuideListener;
import kei.test.plugin.listener.npcinteract.TradeListener;
import kei.test.plugin.meta.MetaKey;

public class TutorialPlugin extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		super.onEnable();

		for (Player player : getServer().getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();

			PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);
			if (playerInfo == null) {
				PlayerManager.getInstance().registerPlayer(uuid);
			}
		}

		// ネームタグを付加されたモブを登録する
		ArrayList<UUID> nameTagMobs = NameTagMobsDB.getInstance().getNameTagMobs();
		for (UUID uuid : nameTagMobs) {
			Entity entity = Bukkit.getEntity(uuid);
			if (entity != null) {
				entity.setMetadata("hasNameTag", new FixedMetadataValue(this, true));
			}
		}

		ArrayList<ChestLock> lockedChestLoc = ChestLockDB.getInstance().getChestLock();
		for (ChestLock c : lockedChestLoc) {
			Location loc = c.getLoc();
			if (loc != null) {
				if(loc.getWorld() != null) {
					if (loc.getBlock() != null) {
						Block b = loc.getBlock();
						b.setMetadata(MetaKey.CHEST_LOCK, new FixedMetadataValue(this, true));
					}
				}
			}
		}

		new WorldCreator("resource").createWorld();
		new WorldCreator("portal").createWorld();
		new WorldCreator("dangeons/dangeon_strange_cave").createWorld();
		new WorldCreator("dangeons/tutorial").createWorld();

		// リスナーの登録
		registerListeners();

		// コマンドの登録
		registerCommands();

		//		new WorldCreator("end_dangeon").createWorld();

	}

	@Override
	public void onDisable() {
		super.onDisable();

		// プレイヤーマネージャから管理対象のプレイヤーを削除する
		for (Player player : getServer().getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			PlayerInfo playerInfo = PlayerManager.getInstance().getPlayerInfo(uuid);
			if (playerInfo != null) {
				WarpPortal warpPortal = PlayerManager.getInstance().getPlayerInfo(uuid).getPortal();
				if (warpPortal != null) {
					warpPortal.breakPortal();
				}

				PlayerManager.getInstance().unregisterPlayer(uuid);
			}
		}
	}

	// リスナーの登録
	private void registerListeners() {
		new LoginListener(this);
		new KillListener(this);
		new InventoryListener(this);
		new PlayerRangeListener(this);
		new PlayerDeathListener(this);
		new BedListener(this);
		new ChatListener(this);
		new PlayerBehaviorListener(this);
		new TradeListener(this);
		new MobSpawnListener(this);
		new BrewerListener(this);
		new LibrarianListener(this);
		new CreeperSpawnListener(this);
		new PlayerDamagedListener(this);
		new ZPigSpawnListener(this);
		new VindicatorSpawnListener(this);
		new GuideListener(this);
		new TipsGuideListener(this);
		new StrangeCaveManager(this);
		new NameTagListener(this);
		new GetExpBottleListener(this);
		new ChestLockListener(this);
		new ItemSortListener(this);
		new AnvilNameTagListener(this);

		// チュートリアル
		new TutorialNPC(this);
		new TutorialDangeonManager(this);
		new TutorialShop(this);

		new CustomRecipe(this);
	}

	// コマンドの登録
	private void registerCommands() {
		getCommand("stats").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("create").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("join").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("command").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("newinfo").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("home").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("init").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("tpblock").setExecutor(new MyPluginCommandExecutor(this));
		getCommand("killMonsters").setExecutor(new MyPluginCommandExecutor(this));

		// 交易関係のモブ召喚
		getCommand("trader").setExecutor(new SummonTraderCommandExecutor(this));
		getCommand("weaponer").setExecutor(new SummonTraderCommandExecutor(this));
		getCommand("mage").setExecutor(new SummonTraderCommandExecutor(this));
		getCommand("promage").setExecutor(new SummonTraderCommandExecutor(this));
		getCommand("library").setExecutor(new SummonTraderCommandExecutor(this));

		// チュートリアルモブの召喚
		getCommand("tutorial").setExecutor(new TutorialNPCSummonCommand(this));

		// 会話するモブ
		getCommand("tipsguide").setExecutor(new SummonTraderCommandExecutor(this));

		// ダンジョン召喚系のモブ
		getCommand("guide").setExecutor(new SummonTraderCommandExecutor(this));
	}

}
