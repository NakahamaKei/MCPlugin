package kei.test.plugin.items;

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

public class WeaponCreator {
	// 武器を生成する
	public static ItemStack create(String name, Material material, List<String> lores, double attack) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", attack,
				Operation.ADD_NUMBER, EquipmentSlot.HAND);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);

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
}
