package huff.lib.helper;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryHelper
{
	public static final String ITEM_BACK = "§7» §cZurück";
	public static final String ITEM_ABORT = "§7» §cAbbrechen";
	public static final String ITEM_CLOSE = "§7» §cSchließen §7«";
	
	private InventoryHelper() { }
	
	public static ItemStack getBackItem()
	{
		ItemStack backItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		backItem.getItemMeta().setDisplayName(ITEM_BACK);
		
		return backItem;
	}
	
	public static ItemStack getAbortItem()
	{
		ItemStack backItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		backItem.getItemMeta().setDisplayName(ITEM_ABORT);
		
		return backItem;
	}
	
	public static ItemStack getCloseItem()
	{
		ItemStack closeItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		closeItem.getItemMeta().setDisplayName(ITEM_CLOSE);
		
		return closeItem;
	}
}
