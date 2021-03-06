package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.identifiers.WauzSkillgemIdentifier;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import net.md_5.bungee.api.ChatColor;

/**
 * A helper class for inserting skillgems into items.
 * 
 * @author Wauzmons
 * 
 * @see WauzSkillgemIdentifier
 */
public class WauzSkillgemInserter {
	
	/**
	 * Tries to insert a skillgem into the given equipment item stack.
	 * Returns false if the item has no skillgem slots or the skillgem is invalid.
	 * 
	 * @param player The player that is inserting the skillgem.
	 * @param equipmentItemStack The equipment that the skillgem is inserted into.
	 * @param skillgemItemStack The skillgem that is getting inserted.
	 * 
	 * @return If the action was successful.
	 */
	public boolean insertSkillgem(Player player, ItemStack equipmentItemStack, ItemStack skillgemItemStack) {
		String skillName = skillgemItemStack.getItemMeta().getDisplayName();
		skillName = StringUtils.substringAfter(skillName, ": " + ChatColor.LIGHT_PURPLE);
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillName);
		
		if(skill != null && EquipmentUtils.hasSkillgemSocket(equipmentItemStack)) {
			ItemMeta itemMeta = equipmentItemStack.getItemMeta();
			List<String> newLores = new ArrayList<>();
			for(String lore : itemMeta.getLore()) {
				if(lore.contains(ChatColor.DARK_RED + "Empty")) {
					newLores.add(ChatColor.WHITE + "Skillgem (" + ChatColor.LIGHT_PURPLE + skill.getSkillId() + ChatColor.WHITE + ")");
					newLores.add(ChatColor.WHITE + skill.getSkillDescription());
					lore = ChatColor.WHITE + skill.getSkillStats();
				}
				newLores.add(lore);
			}
			itemMeta.setLore(newLores);
			equipmentItemStack.setItemMeta(itemMeta);
			return true;
		}
		return false;
	}

}
