package huff.lib.helper;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryHelper
{
	public static final String ITEM_BACK = "§7» §cZurück";
	public static final String ITEM_ABORT = "§7» §cAbbrechen";
	public static final String ITEM_CLOSE = "§7» §cSchließen §7«";
	
	private InventoryHelper() { }
	
	public static @NotNull ItemStack getItemWithMeta(@NotNull Material material, @Nullable String displayName)
	{
		return getItemWithMeta(material, displayName, null);
	}
	
	public static @NotNull ItemStack getItemWithMeta(@NotNull Material material, @Nullable String displayName, @Nullable List<String> lore)
	{
		Validate.notNull((Object) material, "The material cannot be null.");
		
		ItemStack resultItem = new ItemStack(material);
		ItemMeta resultMeta = resultItem.getItemMeta();
		
		if (StringHelper.isNotNullOrEmpty(displayName)) resultMeta.setDisplayName(displayName);	
		if (lore != null) resultMeta.setLore(lore);
			
		resultItem.setItemMeta(resultMeta);
		return resultItem;
	}
	
	public static @NotNull ItemStack getBorderItem()
	{
		return getItemWithMeta(Material.BLACK_STAINED_GLASS_PANE, " ");
	}
	
	public static @NotNull ItemStack getFillItem()
	{
		return getItemWithMeta(Material.WHITE_STAINED_GLASS_PANE, " ");
	}
	
	public static @NotNull ItemStack getBackItem()
	{		
		return getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_BACK);
	}
	
	public static @NotNull ItemStack getAbortItem()
	{
		return getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_ABORT);
	}
	
	public static @NotNull ItemStack getCloseItem()
	{
		return getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_CLOSE);
	}
}
