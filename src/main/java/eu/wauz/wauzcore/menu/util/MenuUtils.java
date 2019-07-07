package eu.wauz.wauzcore.menu.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.EquipmentRuneSocket;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.WauzIdentifier;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class MenuUtils {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");
	
	public static void constructPlayerInventory(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();
		
		setTrashcan(inventory, 1, 2, 3, 4);
		inventory.setItem(0, new ItemStack(Material.END_PORTAL));
		
		WauzDebugger.log(player, "Constructed Player Inventory");
	}
	
	public static void disposePlayerInventory(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();
		
		inventory.setItem(0, null);
		inventory.setItem(1, null);
		inventory.setItem(2, null);
		inventory.setItem(3, null);
		inventory.setItem(4, null);
		
		WauzDebugger.log(player, "Disposed Player Inventory");
	}
	
	public static void setCurrencyDisplay(Inventory menu, Player player, int index) {
		ItemStack currencyItemStack = HeadUtils.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		currencyItemMeta.setDisplayName(ChatColor.GREEN + "Currency");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GOLD + formatter.format(PlayerConfigurator.getCharacterCoins(player))
			+ ChatColor.DARK_PURPLE + " Coins of Wauzland");
		lores.add(ChatColor.GOLD + formatter.format(PlayerConfigurator.getCharacterSoulstones(player))
			+ ChatColor.DARK_PURPLE + " Soulstones");
		lores.add("");
		lores.add(ChatColor.GREEN + "Reputation");
		lores.add(ChatColor.BLUE + formatter.format(PlayerConfigurator.getCharacterRepRepublicWauzland(player))
			+ ChatColor.DARK_PURPLE + " Republic Wauzland");
		lores.add(ChatColor.BLUE + formatter.format(PlayerConfigurator.getCharacterRepEternalEmpire(player))
			+ ChatColor.DARK_PURPLE + " Eternal Empire");
		lores.add(ChatColor.BLUE + formatter.format(PlayerConfigurator.getCharacterRepDarkLegion(player))
			+ ChatColor.DARK_PURPLE + " Dark Legion");
		currencyItemMeta.setLore(lores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(index, currencyItemStack);
	}
	
	public static void setGlobalCurrencyDisplay(Inventory menu, Player player, int index) {
		ItemStack currencyItemStack = HeadUtils.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		currencyItemMeta.setDisplayName(ChatColor.GREEN + "Currency");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GOLD + formatter.format(PlayerConfigurator.getTokens(player))
			+ ChatColor.DARK_PURPLE + " Tokens");
		currencyItemMeta.setLore(lores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(index, currencyItemStack);
	}
	
	public static void setTrashcan(Inventory menu, int... indexes) {
		ItemStack trashcanItemStack = new ItemStack(Material.BARRIER);
		ItemMeta trashcanItemMeta = trashcanItemStack.getItemMeta();
		trashcanItemMeta.setDisplayName(ChatColor.RED + "Trashcan");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.DARK_PURPLE + "Drag Items here, to destroy them.");
		trashcanItemMeta.setLore(lores);
		trashcanItemStack.setItemMeta(trashcanItemMeta);
		for(int index : indexes) {
			menu.setItem(index, trashcanItemStack);
		}
	}
	
	public static void setComingSoon(Inventory menu, String lore, int index) {
		ItemStack soonItemStack = new ItemStack(Material.SIGN);
		ItemMeta soonItemMeta = soonItemStack.getItemMeta();
		soonItemMeta.setDisplayName(ChatColor.RED + "Coming Soon");
		if(StringUtils.isNotBlank(lore)) {
			List<String> lores = new ArrayList<String>();
			lores.add(lore);
			soonItemMeta.setLore(lores);
		}
		soonItemStack.setItemMeta(soonItemMeta);
		menu.setItem(index, soonItemStack);
	}
	
	public static void setBorders(Inventory menu) {
		ItemStack borderItemStack = new ItemStack(Material.IRON_BARS);
		ItemMeta borderItemMeta = borderItemStack.getItemMeta();
		borderItemMeta.setDisplayName(" ");
		borderItemStack.setItemMeta(borderItemMeta);
		
		for(int slot = 0; slot < menu.getSize(); slot++) {
			if(menu.getItem(slot) == null)
				menu.setItem(slot, borderItemStack);
		}
	}
	
	public static List<Material> staticItems = new ArrayList<>();
	
	public static List<Material> validScrollMaterials = new ArrayList<Material>
			(Arrays.asList(Material.NAME_TAG, Material.FIREWORK_STAR, Material.REDSTONE));

	public static void onSpecialItemInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		boolean numberKeyPressed = event.getClick().equals(ClickType.NUMBER_KEY);
		ItemStack hotbarItem = numberKeyPressed ? event.getClickedInventory().getItem(event.getHotbarButton()) : null;
		if(hotbarItem != null) {
			if(staticItems.contains(hotbarItem.getType())) {
				event.setCancelled(true);
				return;
			}
				
			String itemName = (!hotbarItem.hasItemMeta() || !hotbarItem.getItemMeta().hasDisplayName())
					? "" : hotbarItem.getItemMeta().getDisplayName();
			if(itemName.contains("Cosmetic Item")) {
				event.setCancelled(true);
				return;
			}
		}
		ItemStack itemStack = event.getCurrentItem();
		if(itemStack != null) {
			if(staticItems.contains(itemStack.getType())) {
				event.setCancelled(true);
				if(itemStack.getType().equals(Material.NETHER_STAR))
					WauzMenu.open(player);
				return;
			}
				
			String itemName = (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName())
					? "" : itemStack.getItemMeta().getDisplayName();
			if(itemName.contains("Cosmetic Item")) {
				event.setCancelled(true);
				return;
			}
			
			ItemStack scroll = (player.getItemOnCursor());
			if(!validScrollMaterials.contains(scroll.getType()))
				return;
			
			String scrollName = scroll.getItemMeta().getDisplayName();
			
			if(scrollName.contains("Scroll of Wisdom")) {
				if(!itemName.contains("Unidentified") || itemName.contains("Scroll"))
						return;			
				
				event.setCancelled(true);
				scroll.setAmount(scroll.getAmount() - 1);
				if(itemName.contains("Item")) WauzIdentifier.identifyItem(event);
				else if(itemName.contains("Rune")) WauzIdentifier.identifyRune(event);
				else if(itemName.contains("Map")) WauzIdentifier.identifyShrine(event);
				else if(itemName.contains("Skillgem")) WauzIdentifier.identifySkillgem(event);
			}
			
			else if(scrollName.contains("Scroll of Fortune")) {
				if(itemName.contains("Scroll"))
					return;
				
				if(ShopBuilder.sell((Player) player, itemStack, false)) {
					scroll.setAmount(scroll.getAmount() - 1);
					event.setCancelled(true);
				}	
			}
			
			else if(scrollName.contains("Scroll of Toughness")) {
				if(itemName.contains("Scroll"))
					return;
				
				if(ShopBuilder.repair((Player) player, itemStack, false)) {
					scroll.setAmount(scroll.getAmount() - 1);
					event.setCancelled(true);
				}
			}
			
			else if(scrollName.contains("Scroll of Regret")) {
				if(itemName.contains("Scroll"))
					return;
				
				if(EquipmentRuneSocket.clearAllSockets(event)) {
					scroll.setAmount(scroll.getAmount() - 1);
					event.setCancelled(true);
				}
			}
			
			else if(scrollName.contains("Rune")) {
				if(!scrollName.contains("Unidentified") && EquipmentRuneSocket.insertRune(event)) {
					scroll.setAmount(scroll.getAmount() - 1);
					event.setCancelled(true);
				}
			}
			
			else if(scrollName.contains("Skillgem")) {
				if(!scrollName.contains("Unidentified") && EquipmentRuneSocket.insertSkillgem(event)) {
					scroll.setAmount(scroll.getAmount() - 1);
					event.setCancelled(true);
				}
			}
		}
	}
	
	public static void onScrollItemInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack scroll = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.hasDisplayName(scroll))
			return;
		
		String scrollName = scroll.getItemMeta().getDisplayName();
		
		if(scrollName.contains("Scroll of Summoning")) {
			PetOverviewMenu.addPet(event);
		}
		
		else if(scrollName.contains("Scroll of Comfort")) {
			new WauzPlayerEventHomeChange(player, scroll).execute(player);
		}
	}

	public static void onStaticItemDrop(PlayerDropItemEvent event) {
		if(staticItems.contains(event.getItemDrop().getItemStack().getType()))
			event.setCancelled(true);
	}
	
	public static void onStaticItemSwap(PlayerSwapHandItemsEvent event) {
		Material mainHandType = event.getMainHandItem().getType();
		Material offHandType = event.getOffHandItem().getType();
		if(staticItems.contains(mainHandType) || staticItems.contains(offHandType))
			event.setCancelled(true);
	}

}
