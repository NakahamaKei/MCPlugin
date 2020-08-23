package kei.test.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KillCounter {

	private final String ZOMBIE = "zombie";
	private final String SKELETON = "skeleton";
	private final String CREEPER = "creeper";
	private final String SPIDER = "spider";
	private final String SLIME = "slime";
	private final String ENDERMAN = "enderman";
	private final String BLAZE = "blaze";
	private final String GHAST = "ghast";

	private HashMap<String, ItemStack> mobIcons = new LinkedHashMap<>();
	private PlayerInfo playerInfo;


	public KillCounter(UUID uuid) {
		playerInfo = new PlayerInfo(uuid);

		setIcons();
		setDisplayNames();
		setLores();
	}

	public ItemStack getItemStack(String key) {
		return this.mobIcons.get(key);
	}

	private void setIcons() {
		mobIcons.put(ZOMBIE, new ItemStack(Material.ZOMBIE_HEAD, 1));
		mobIcons.put(SKELETON, new ItemStack(Material.SKELETON_SKULL, 1));
		mobIcons.put(CREEPER, new ItemStack(Material.CREEPER_HEAD, 1));
		mobIcons.put(SPIDER, new ItemStack(Material.STRING, 1));
		mobIcons.put(SLIME, new ItemStack(Material.SLIME_BLOCK, 1));
		mobIcons.put(ENDERMAN, new ItemStack(Material.ENDER_EYE, 1));
		mobIcons.put(BLAZE, new ItemStack(Material.BLAZE_ROD, 1));
		mobIcons.put(GHAST, new ItemStack(Material.GHAST_TEAR, 1));
	}

	private void setDisplayNames() {
		ItemMeta meta;

		meta = mobIcons.get(ZOMBIE).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "ゾンビ討伐数");
		mobIcons.get(ZOMBIE).setItemMeta(meta);

		meta = mobIcons.get(SKELETON).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "スケルトン討伐数");
		mobIcons.get(SKELETON).setItemMeta(meta);

		meta = mobIcons.get(CREEPER).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "クリーパー討伐数");
		mobIcons.get(CREEPER).setItemMeta(meta);

		meta = mobIcons.get(SPIDER).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "スパイダー討伐数");
		mobIcons.get(SPIDER).setItemMeta(meta);

		meta = mobIcons.get(SLIME).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "スライム討伐数");
		mobIcons.get(SLIME).setItemMeta(meta);

		meta = mobIcons.get(ENDERMAN).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "エンダーマン討伐数");
		mobIcons.get(ENDERMAN).setItemMeta(meta);

		meta = mobIcons.get(BLAZE).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "ブレイズ討伐数");
		mobIcons.get(BLAZE).setItemMeta(meta);

		meta = mobIcons.get(GHAST).getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "ガスト討伐数");
		mobIcons.get(GHAST).setItemMeta(meta);

	}

	private void setLores() {
		ItemMeta meta;
		List<String> lores = new ArrayList<>();

		meta = mobIcons.get(ZOMBIE).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(ZOMBIE) + "体");
		meta.setLore(lores);
		mobIcons.get(ZOMBIE).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(SKELETON).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(SKELETON) + "体");
		meta.setLore(lores);
		mobIcons.get(SKELETON).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(CREEPER).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(CREEPER) + "体");
		meta.setLore(lores);
		mobIcons.get(CREEPER).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(SPIDER).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(SPIDER) + "体");
		meta.setLore(lores);
		mobIcons.get(SPIDER).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(SLIME).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(SLIME) + "体");
		meta.setLore(lores);
		mobIcons.get(SLIME).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(ENDERMAN).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(ENDERMAN) + "体");
		meta.setLore(lores);
		mobIcons.get(ENDERMAN).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(BLAZE).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(BLAZE) + "体");
		meta.setLore(lores);
		mobIcons.get(BLAZE).setItemMeta(meta);
		lores.clear();

		meta = mobIcons.get(GHAST).getItemMeta();
		lores.add(ChatColor.GREEN + playerInfo.getKillCountString(GHAST) + "体");
		meta.setLore(lores);
		mobIcons.get(GHAST).setItemMeta(meta);
		lores.clear();
	}
}
