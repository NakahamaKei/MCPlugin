package kei.test.plugin;

import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import kei.test.plugin.meta.MonsterNameMetaKey;
import kei.test.plugin.monster.MonsterName;

public class MobName {
	private static MobName singleton = new MobName();
	private HashMap<EntityType, String> nameMap = new HashMap<>();

	private MobName() {
		nameMap.put(EntityType.BAT, "バット");
		nameMap.put(EntityType.BEE, "ミツバチ");
		nameMap.put(EntityType.BLAZE, "ブレイズ");
		nameMap.put(EntityType.CAT, "ネコ");
		nameMap.put(EntityType.CAVE_SPIDER, "");
		nameMap.put(EntityType.CHICKEN, "ニワトリ");
		nameMap.put(EntityType.COD, "タラ");
		nameMap.put(EntityType.COW, "ウシ");
		nameMap.put(EntityType.CREEPER, "匠");
		nameMap.put(EntityType.DOLPHIN, "イルカ");
		nameMap.put(EntityType.DONKEY, "ロバ");
		nameMap.put(EntityType.DROWNED, "ドラウンド");
		nameMap.put(EntityType.ELDER_GUARDIAN, "エルダーガーディアン");
		nameMap.put(EntityType.ENDER_DRAGON, "エンダードラゴン");
		nameMap.put(EntityType.ENDERMAN, "エンダーマン");
		nameMap.put(EntityType.ENDERMITE, "エンダーマイト");
		nameMap.put(EntityType.EVOKER, "エヴォーカー");
		nameMap.put(EntityType.FOX, "キツネ");
		nameMap.put(EntityType.GHAST, "ガスト");
		nameMap.put(EntityType.GIANT, "ジャイアント");
		nameMap.put(EntityType.GUARDIAN, "ガーディアン");
		nameMap.put(EntityType.HOGLIN, "ホグリン");
		nameMap.put(EntityType.HORSE, "ウマ");
		nameMap.put(EntityType.HUSK, "ハスク");
		nameMap.put(EntityType.ILLUSIONER, "イリュージョナー");
		nameMap.put(EntityType.IRON_GOLEM, "アイアンゴーレム");
		nameMap.put(EntityType.LLAMA, "ラマ");
		nameMap.put(EntityType.MAGMA_CUBE, "マグマキューブ");
		nameMap.put(EntityType.MULE, "ラバ");
		nameMap.put(EntityType.MUSHROOM_COW, "マッシュルームカウ");
		nameMap.put(EntityType.OCELOT, "ヤマネコ");
		nameMap.put(EntityType.PANDA, "パンダ");
		nameMap.put(EntityType.PARROT, "オウム");
		nameMap.put(EntityType.PHANTOM, "ファントム");
		nameMap.put(EntityType.PIG, "ブタ");
		nameMap.put(EntityType.PIGLIN, "ピグリン");
		nameMap.put(EntityType.PILLAGER, "ピレジャー");
		nameMap.put(EntityType.POLAR_BEAR, "シロクマ");
		nameMap.put(EntityType.PUFFERFISH, "フグ");
		nameMap.put(EntityType.RABBIT, "ウサギ");
		nameMap.put(EntityType.RAVAGER, "");
		nameMap.put(EntityType.SALMON, "シャケ");
		nameMap.put(EntityType.SHEEP, "ヒツジ");
		nameMap.put(EntityType.SHULKER, "シュルカー");
		nameMap.put(EntityType.SILVERFISH, "シルバーフィッシュ");
		nameMap.put(EntityType.SKELETON, "スケルトン");
		nameMap.put(EntityType.SKELETON_HORSE, "スケルトンホース");
		nameMap.put(EntityType.SLIME, "スライム");
		nameMap.put(EntityType.SNOWMAN, "スノーマン");
		nameMap.put(EntityType.SPIDER, "スパイダー");
		nameMap.put(EntityType.SQUID, "イカ");
		nameMap.put(EntityType.STRAY, "ストレイ");
		nameMap.put(EntityType.STRIDER, "ストライダー");
		nameMap.put(EntityType.TRADER_LLAMA, "商人のラマ");
		nameMap.put(EntityType.TROPICAL_FISH, "熱帯魚");
		nameMap.put(EntityType.TURTLE, "カメ");
		nameMap.put(EntityType.VEX, "ヴェックス");
		nameMap.put(EntityType.VILLAGER, "村人");
		nameMap.put(EntityType.VINDICATOR, "ヴィンディケイター");
		nameMap.put(EntityType.WANDERING_TRADER, "行商人");
		nameMap.put(EntityType.WITCH, "ウィッチ");
		nameMap.put(EntityType.WITHER, "ウィザー");
		nameMap.put(EntityType.WITHER_SKELETON, "ウィザースケルトン");
		nameMap.put(EntityType.WOLF, "オオカミ");
		nameMap.put(EntityType.ZOGLIN, "ゾグリン");
		nameMap.put(EntityType.ZOMBIE, "ゾンビ");
		nameMap.put(EntityType.ZOMBIE_HORSE, "ゾンビホース");
		nameMap.put(EntityType.ZOMBIE_VILLAGER, "ゾンビ村人");
		nameMap.put(EntityType.ZOMBIFIED_PIGLIN, "ゾンビピグリン");
	}

	public static MobName getInstance() {
		return singleton;
	}

	public String getName(LivingEntity entity) {
		if (entity.hasMetadata(MonsterNameMetaKey.LAPIS_ZOMBIE)) {
			return MonsterName.LAPIS_ZOMBIE;
		} else if (entity.hasMetadata(MonsterNameMetaKey.BOSS_LAPIS_ZOMBIE)) {
			return MonsterName.HEAVY_LAPIS_ZOMBIE;
		} else if (entity.hasMetadata(MonsterNameMetaKey.ROCK_ZOMBIE)) {
			return MonsterName.ROCK_ZOMBIE;
		} else {
			return nameMap.get(entity.getType());
		}
	}
}
