package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class containing static inventory methods.
 * Covers everything around "org.bukkit.inventory.Inventory".
 */
public class InventoryHelper
{
	public static final int INV_SIZE_1 = 9;
	public static final int INV_SIZE_2 = 18;
	public static final int INV_SIZE_3 = 27;
	public static final int INV_SIZE_4 = 36;
	public static final int INV_SIZE_5 = 45;
	public static final int INV_SIZE_6 = 54;

	public static final int ROW_LENGTH = 9;
	public static final int LAST_ROW = 6;
	
	public static final String ITEM_BACK = "§7» §cZurück";
	public static final String ITEM_ABORT = "§7» §cAbbrechen";
	public static final String ITEM_CLOSE = "§7» §cSchließen §7«";
	
	private InventoryHelper() { }
	
	public static @NotNull ItemStack getBorderItem()
	{
		return ItemHelper.getItemWithMeta(Material.BLACK_STAINED_GLASS_PANE, null, null, ItemFlag.HIDE_ATTRIBUTES);
	}
	
	public static @NotNull ItemStack getFillItem()
	{
		return ItemHelper.getItemWithMeta(Material.WHITE_STAINED_GLASS_PANE, null, null, ItemFlag.HIDE_ATTRIBUTES);
	}
	
	public static @NotNull ItemStack getBackItem()
	{		
		return ItemHelper.getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_BACK);
	}
	
	public static @NotNull ItemStack getAbortItem()
	{
		return ItemHelper.getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_ABORT);
	}
	
	public static @NotNull ItemStack getCloseItem()
	{
		return ItemHelper.getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_CLOSE);
	}
	
	public static void setBorder(@NotNull Inventory inventory, @Nullable ItemStack borderItemStack)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");
		
		final int inventorySize = inventory.getSize();
		final int lastLineIndex = inventorySize - ROW_LENGTH;
		
		for (int i = 0; i < inventorySize; i++)
		{
			if (i < ROW_LENGTH || i % ROW_LENGTH == 0 || (i + 1) % ROW_LENGTH == 0 || i >= lastLineIndex)
			{
				inventory.setItem(i, borderItemStack);
			}
		}
	}
	
	public static void setFill(@NotNull Inventory inventory, @Nullable ItemStack fillItemStack, boolean ignoreBorder)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");
		
		final int inventorySize = inventory.getSize();
		final int lastLineIndex = inventorySize - ROW_LENGTH;
		
		for (int i = 0; i < inventorySize; i++)
		{
			if (ignoreBorder || (i >= ROW_LENGTH && i % ROW_LENGTH != 0 && (i + 1) % ROW_LENGTH != 0 && i < lastLineIndex))
			{
				inventory.setItem(i, fillItemStack);
			}		
		}
	}
	
	public static void setItem(@NotNull Inventory inventory, int row, int coloum, @Nullable ItemStack itemStack)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
	
		inventory.setItem(getPositonFromRowColoum(inventory.getSize(), row, coloum), itemStack);
	}
	
	public static @Nullable ItemStack getItem(@NotNull Inventory inventory, int row, int coloum)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
		
		return inventory.getItem(getPositonFromRowColoum(inventory.getSize(), row, coloum));
	}
	
	private static int getPositonFromRowColoum(int inventorySize, int row, int coloum)
	{
		if (row * ROW_LENGTH > inventorySize)
		{
			row = getLastLine(inventorySize);
		}
		
		if (coloum > ROW_LENGTH)
		{
			coloum = ROW_LENGTH;
		}
		row--;
		coloum--;
		
		if (row < 0)
		{
			row = 0;
		}
		
		if (coloum < 0)
		{
			coloum = 0;
		}	
		return ((row) * ROW_LENGTH) + coloum;
	}
	
	public static int getLastLine(int inventorySize)
	{			
		if (inventorySize % ROW_LENGTH != 0)
		{
			throw new NumberFormatException(StringHelper.build("The inventory-size is not a multiply of ", ROW_LENGTH, "."));
		}	
		return inventorySize / ROW_LENGTH;
	}
	
	/**
	 * 
	 * @param inventory
	 * @param itemStack
	 * @return
	 */
	public static int getFreeItemStackAmount(@NotNull Inventory inventory, @NotNull ItemStack itemStack)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
		Validate.notNull((Object) itemStack, "The item-stack cannot be null.");	
		
		final int maxStackSize = inventory.getMaxStackSize() < itemStack.getMaxStackSize() ? inventory.getMaxStackSize() : itemStack.getMaxStackSize();
		int freeItemStackAmount = 0;
		
		for (ItemStack currentItemStack : inventory.getStorageContents())
		{			
			if (currentItemStack == null || currentItemStack.getType() == Material.AIR)
			{
				freeItemStackAmount += maxStackSize;
			}
			else if (currentItemStack.isSimilar(itemStack) && currentItemStack.getAmount() < maxStackSize)
			{
				freeItemStackAmount += maxStackSize - currentItemStack.getAmount();
			}
		}
		return freeItemStackAmount;
	}
	
	/**
	 * 
	 * @param inventory
	 * @param itemStack
	 * @return
	 */
	public static int getFreeItemStackSpace(@NotNull Inventory inventory, @NotNull ItemStack itemStack)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
		Validate.notNull((Object) itemStack, "The item-stack cannot be null.");	
		
		final int maxStackSize = inventory.getMaxStackSize() < itemStack.getMaxStackSize() ? inventory.getMaxStackSize() : itemStack.getMaxStackSize();
		int openAmount = itemStack.getAmount();
		int freeItemStackSpace = 0;
		
		for (ItemStack currentItemStack : inventory.getStorageContents())
		{			
			if (currentItemStack == null || currentItemStack.getType() == Material.AIR)
			{
				freeItemStackSpace++;
			}
			else if (currentItemStack.isSimilar(itemStack) && currentItemStack.getAmount() < maxStackSize)
			{
				openAmount -= maxStackSize - currentItemStack.getAmount();
			}
		}
		
		if (openAmount > 0)
		{
			freeItemStackSpace--;
		}		
		return freeItemStackSpace;
	}
	
	public static boolean isContainerInventory(@NotNull InventoryType inventoryType)
	{
		return inventoryType == InventoryType.PLAYER || inventoryType == InventoryType.CHEST || inventoryType == InventoryType.BARREL ||
			   inventoryType == InventoryType.SHULKER_BOX || inventoryType == InventoryType.ENDER_CHEST || inventoryType == InventoryType.HOPPER ||
			   inventoryType == InventoryType.DISPENSER || inventoryType == InventoryType.DROPPER;
	}
	
	public static boolean isPickupAction(InventoryAction inventoryAction)
	{
		return inventoryAction == InventoryAction.PICKUP_ALL || inventoryAction == InventoryAction.PICKUP_HALF || 
			   inventoryAction == InventoryAction.PICKUP_ONE || inventoryAction == InventoryAction.PICKUP_SOME ||
			   inventoryAction == InventoryAction.MOVE_TO_OTHER_INVENTORY;
	}
	
	public static boolean isPlaceAction(InventoryAction inventoryAction)
	{
		return inventoryAction == InventoryAction.PLACE_ALL || inventoryAction == InventoryAction.PLACE_SOME || 
			   inventoryAction == InventoryAction.PLACE_ONE;
	}
}
