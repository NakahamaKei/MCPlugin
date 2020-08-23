package kei.test.plugin.items;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ProtectorCreator {
	public static ItemStack createProtector(String name) {
		ItemStack protector = null;
		if (name == ProtectorName.IRON_HELM) {
			protector = create(ProtectorName.IRON_HELM, Material.IRON_HELMET,
					Arrays.asList("鉄製の頭防具", "装備すると敵からのダメージを", "軽減することができる"), EquipmentSlot.HEAD, 2);
		} else if (name == ProtectorName.IRON_MAIL) {
			protector = create(ProtectorName.IRON_MAIL, Material.IRON_CHESTPLATE,
					Arrays.asList("鉄製の胸防具", "装備すると敵からのダメージを", "軽減することができる"), EquipmentSlot.CHEST, 6);
		} else if (name == ProtectorName.IRON_LEGGINGS) {
			protector = create(ProtectorName.IRON_LEGGINGS, Material.IRON_LEGGINGS,
					Arrays.asList("鉄製の脚防具", "装備すると敵からのダメージを", "軽減することができる"), EquipmentSlot.LEGS, 5);
		} else if (name == ProtectorName.IRON_BOOTS) {
			protector = create(ProtectorName.IRON_BOOTS, Material.IRON_BOOTS,
					Arrays.asList("鉄製の足防具", "装備すると敵からのダメージを", "軽減することができる"), EquipmentSlot.FEET, 2);
		}

		return protector;
	}

	// 武器を生成する
	private static ItemStack create(String name, Material material, List<String> lores, EquipmentSlot slot,
			double defence) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		AttributeModifier modifier = null;

		if (slot == EquipmentSlot.HEAD) {
			modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", defence,
					Operation.ADD_NUMBER, EquipmentSlot.HEAD);
		} else if (slot == EquipmentSlot.CHEST) {
			modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", defence,
					Operation.ADD_NUMBER, EquipmentSlot.CHEST);
		} else if (slot == EquipmentSlot.LEGS) {
			modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", defence,
					Operation.ADD_NUMBER, EquipmentSlot.LEGS);
		} else if (slot == EquipmentSlot.FEET) {
			modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", defence,
					Operation.ADD_NUMBER, EquipmentSlot.FEET);
		} else {
			return null;
		}

		meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);

		if (!meta.getDisplayName().contains(ChatColor.GREEN + "")) {
			meta.setDisplayName(ChatColor.GREEN + name);
		}

		if (lores != null) {
			for (int i = 0; i < lores.size(); i++) {
				lores.set(i, ChatColor.WHITE + lores.get(i));
			}
			meta.setLore(lores);
		}
		item.setItemMeta(meta);

		return item;
	}

}
