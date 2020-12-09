package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.various.MenuHolder;

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
	
	/**
	 * Gets the menu inventory border item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	public static @NotNull ItemStack getBorderItem()
	{
		return ItemHelper.getItemWithMeta(Material.BLACK_STAINED_GLASS_PANE, null, null, ItemFlag.HIDE_ATTRIBUTES);
	}
	
	/**
	 * Gets the menu inventory fill item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	public static @NotNull ItemStack getFillItem()
	{
		return ItemHelper.getItemWithMeta(Material.WHITE_STAINED_GLASS_PANE, null, null, ItemFlag.HIDE_ATTRIBUTES);
	}
	
	/**
	 * Gets the menu inventory back item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	public static @NotNull ItemStack getBackItem()
	{		
		return ItemHelper.getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_BACK);
	}
	
	/**
	 * Gets the menu inventory abort item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	public static @NotNull ItemStack getAbortItem()
	{
		return ItemHelper.getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_ABORT);
	}
	
	/**
	 * Gets the menu inventory close item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	public static @NotNull ItemStack getCloseItem()
	{
		return ItemHelper.getItemWithMeta(Material.RED_STAINED_GLASS_PANE, ITEM_CLOSE);
	}
	
	/**
	 * Sets the border in the given inventory with the specified item. 
	 * 
	 * @param   inventory         the inventory to set the border in
	 * @param   borderItemStack   the item that is set as border
	 */
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
	
	/**
	 * Sets the filling in the given inventory with the specified item. 
	 * Can also fill the border area.
	 * 
	 * @param   inventory       the inventory to fill
	 * @param   fillItemStack   the item that is set as filling
	 * @param   ignoreBorder    a boolean if the border area should also be filled
	 */
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
	
	/**
	 * Sets the given item stack to the target slot in the given inventory.
	 * The target slot gets located by the row and column value.
	 * 
	 * @param   inventory       the inventory to set the item stack in
	 * @param   row             the row to the target slot
	 * @param   column          the column to the target slot
	 * @param   itemStack       the item stack to set in the target slot
	 */
	public static void setItem(@NotNull Inventory inventory, int row, int column, @Nullable ItemStack itemStack)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
	
		inventory.setItem(getPositonFromRowcolumn(inventory.getSize(), row, column), itemStack);
	}
	
	/**
	 * Gets the item stack from the target slot in the given inventory.
	 * The target slot gets located by the row and column value.
	 * 
	 * @param   inventory   the inventory to get the item stack from
	 * @param   row         the row to the target slot
	 * @param   column      the column to the target slot
	 * @return              The item stack at the target slot.
	 */
	public static @Nullable ItemStack getItem(@NotNull Inventory inventory, int row, int column)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
		
		return inventory.getItem(getPositonFromRowcolumn(inventory.getSize(), row, column));
	}
	
	/**
	 * Gets the last row from the a specified inventory size.
	 * 
	 * @param   inventorySize   the inventory size that has to be a multiply of "ROW_LENGTH"
	 * @return                  The last row in a inventory with the specified size.
	 */
	public static int getLastLine(int inventorySize)
	{			
		if (inventorySize % ROW_LENGTH != 0)
		{
			throw new NumberFormatException(StringHelper.build("The inventory-size is not a multiply of ", ROW_LENGTH, "."));
		}	
		return inventorySize / ROW_LENGTH;
	}
	
	/**
	 * Checks how much amount of a specified item stack can be added to a given inventory.
	 * 
	 * @param   inventory   the inventory to check free item stack amount in
	 * @param   itemStack   the item stack with that the check is processed
	 * @return              The summed up free item stack amount.
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
	 * Checks how many free inventory slots in the given inventory exist.
	 * 
	 * @param   inventory   the inventory to check free slots in
	 * @return              The free slots. 
	 */
	public static int getFreeSlots(@NotNull Inventory inventory)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");
		
		int freeSlots = 0;
		
		for (ItemStack currentItemStack : inventory.getStorageContents())
		{			
			if (currentItemStack == null || currentItemStack.getType() == Material.AIR)
			{
				freeSlots++;
			}
		}	
		return freeSlots;
	}
	
	/**
	 * Checks how many free inventory slots exist if the specified item stack is added to the given inventory.
	 * 
	 * @param   inventory   the inventory to check free slots in
	 * @param   itemStack   the item stack with that the check is processed
	 * @return              The free slots if the given item stack will be added. 
	 */
	public static int getFreeSlotsAfterAdding(@NotNull Inventory inventory, @NotNull ItemStack itemStack)
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
	
	/**
	 * Checks if the given inventory type is a container.
	 * Means that the inventory only has storage function.
	 * 
	 * @param   inventoryType   the inventory type to check
	 * @return                  The check result.
	 */
	public static boolean isContainerInventory(@NotNull InventoryType inventoryType)
	{
		return inventoryType == InventoryType.PLAYER || inventoryType == InventoryType.CHEST || inventoryType == InventoryType.BARREL ||
			   inventoryType == InventoryType.SHULKER_BOX || inventoryType == InventoryType.ENDER_CHEST || inventoryType == InventoryType.HOPPER ||
			   inventoryType == InventoryType.DISPENSER || inventoryType == InventoryType.DROPPER;
	}
	
	/**
	 * Checks if the given inventory action is a pickup.
	 * Means that a item stack were picked up from a slot or moved with shift in directly to another inventory.
	 * 
	 * @param   inventoryType   the inventory action to check
	 * @return                  The check result.
	 */
	public static boolean isPickupAction(InventoryAction inventoryAction)
	{
		return inventoryAction == InventoryAction.PICKUP_ALL || inventoryAction == InventoryAction.PICKUP_HALF || 
			   inventoryAction == InventoryAction.PICKUP_ONE || inventoryAction == InventoryAction.PICKUP_SOME ||
			   inventoryAction == InventoryAction.MOVE_TO_OTHER_INVENTORY;
	}
	
	/**
	 * Checks if the given inventory action is a place.
	 * Means that a picked up item stack were placed to a slot.
	 * 
	 * @param   inventoryType   the inventory action to check
	 * @return                  The check result.
	 */
	public static boolean isPlaceAction(InventoryAction inventoryAction)
	{
		return inventoryAction == InventoryAction.PLACE_ALL || inventoryAction == InventoryAction.PLACE_SOME || 
			   inventoryAction == InventoryAction.PLACE_ONE;
	}
	
	/**
	 * Checks if a holder of an inventory equals the given class.
	 * If so the holder gets casted and returned.
	 * If not so null will be returned.
	 * 
	 * @param   <T>           a type extending from "huff.lib.various.MenuHolder"
	 * @param   inventory     the inventory the holder holds
	 * @param   holderClass   a class from the described type parameter
	 * @return                The menu holder as specified type.
	 */
	public static @Nullable <T extends MenuHolder> T getMenuHolder(@NotNull Inventory inventory, Class<T> holderClass)
	{
		final InventoryHolder inventoryHolder = inventory.getHolder(); 
		
		if (holderClass.isInstance(inventoryHolder))
		{
			return (T) inventoryHolder;
		}
		return null;
	}
	
	private static int getPositonFromRowcolumn(int inventorySize, int row, int column)
	{
		if (row * ROW_LENGTH > inventorySize)
		{
			row = getLastLine(inventorySize);
		}
		
		if (column > ROW_LENGTH)
		{
			column = ROW_LENGTH;
		}
		row--;
		column--;
		
		if (row < 0)
		{
			row = 0;
		}
		
		if (column < 0)
		{
			column = 0;
		}	
		return ((row) * ROW_LENGTH) + column;
	}
}
