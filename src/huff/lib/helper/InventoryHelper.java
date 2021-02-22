package huff.lib.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.menuholder.MenuHolder;
import huff.lib.various.comparator.SlotItemValueComparator;
import huff.lib.various.structures.Pair;

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
	public static final int INV_SIZE_INTERNAL = 46;
	
	public static final int ROW_LENGTH = 9;
	public static final int LAST_ROW = 6;

	public static final int SECONDARY_HAND = 45;
	
	public static final String ITEM_BACK = "§7» §cZurück";
	public static final String ITEM_ABORT = "§7» §cAbbrechen";
	public static final String ITEM_CLOSE = "§7» §cSchließen §7«";
	
	public static final Material MATERIAL_BORDER = Material.BLACK_STAINED_GLASS_PANE;
	public static final Material MATERIAL_FILL = Material.WHITE_STAINED_GLASS_PANE;
	public static final Material MATERIAL_RETURN = Material.RED_STAINED_GLASS_PANE;
	
	private InventoryHelper() { }
	
	/**
	 * Gets the menu inventory border item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	@NotNull
	public static  ItemStack getBorderItem()
	{
		return ItemHelper.getItemWithMeta(MATERIAL_BORDER, " ");
	}
	
	/**
	 * Gets the menu inventory fill item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	@NotNull
	public static ItemStack getFillItem()
	{
		return ItemHelper.getItemWithMeta(MATERIAL_FILL, " ");
	}
	
	/**
	 * Gets the menu inventory back item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	@NotNull
	public static ItemStack getBackItem()
	{		
		return ItemHelper.getItemWithMeta(MATERIAL_RETURN, ITEM_BACK);
	}
	
	/**
	 * Gets the menu inventory abort item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	@NotNull
	public static ItemStack getAbortItem()
	{
		return ItemHelper.getItemWithMeta(MATERIAL_RETURN, ITEM_ABORT);
	}
	
	/**
	 * Gets the menu inventory close item.
	 * 
	 * @return   The menu inventory item as "org.bukkit.inventory.ItemStack".
	 */
	@NotNull
	public static ItemStack getCloseItem()
	{
		return ItemHelper.getItemWithMeta(MATERIAL_RETURN, ITEM_CLOSE);
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
	
		inventory.setItem(getSlotFromRowColumn(inventory.getSize(), row, column), itemStack);
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
	@Nullable
	public static ItemStack getItem(@NotNull Inventory inventory, int row, int column)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");	
		
		return inventory.getItem(getSlotFromRowColumn(inventory.getSize(), row, column));
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
	 * @return              The free slot count. 
	 */
	public static int getFreeSlotCount(@NotNull Inventory inventory)
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
	 * Gets all free slots from the specified inventory in the given inventory view.
	 * 
	 * @param   inventoryView   the inventory view containing the inventory to get the free slots from
	 * @param   topInventory    determines which inventory from the inventory view will be used 
	 * @return                  The free slots. 
	 */
	@NotNull
	public static List<Integer> getFreeSlots(@NotNull InventoryView inventoryView, boolean topInventory)
	{
		return getFreeSlots(inventoryView, topInventory, null);
	}
	
	/**
	 * Gets all free or compatible slots from the specified inventory in the given inventory view.
	 * 
	 * @param   inventoryView   the inventory view containing the inventory to get the free slots from
	 * @param   topInventory    determines which inventory from the inventory view will be used 
	 * @param   itemStack       the item with which the compatibility is checked
	 * @return                  The free and compatible slots.
	 */
	@NotNull
	public static List<Integer> getFreeSlots(@NotNull InventoryView inventoryView, boolean topInventory, @Nullable ItemStack itemStack)
	{
		Validate.notNull((Object) inventoryView, "The inventory view cannot be null.");
		
		final List<Integer> freeSlots = new ArrayList<>();
		final List<Integer> storageSlots = getStorageSlots(inventoryView, topInventory);
		final Inventory inventory = topInventory ? inventoryView.getTopInventory() : inventoryView.getBottomInventory();
		
		for (int slot : storageSlots)
		{
			final ItemStack currentItemStack = inventory.getItem(slot);
			
			if (currentItemStack == null || currentItemStack.getType() == Material.AIR ||
			    (currentItemStack.isSimilar(itemStack) && currentItemStack.getAmount() < currentItemStack.getMaxStackSize()))
			{
				freeSlots.add(slot);
			}
		}
		return freeSlots;
	}
	
	@NotNull
	public static List<Integer> getStorageSlots(@NotNull InventoryView inventoryView, boolean topInventory)
	{
		Validate.notNull((Object) inventoryView, "The inventory view cannot be null");
		
		final List<Integer> slots = new ArrayList<>();
		final int startIndex = topInventory ? 0 : inventoryView.getTopInventory().getSize();
		final int size = topInventory ? inventoryView.getTopInventory().getSize() : inventoryView.countSlots();
		
		for (int i = startIndex; i < size; i++)
		{
			if (inventoryView.getSlotType(i) == SlotType.CONTAINER || (inventoryView.getSlotType(i) == SlotType.QUICKBAR && i != SECONDARY_HAND))
			{
				slots.add(inventoryView.convertSlot(i));
			}	
		}
		return slots;
	}
	
	/**
	 * Checks how many free inventory slots exist if the specified item stack is added to the given inventory.
	 * 
	 * @param   inventory   the inventory to check free slots in
	 * @param   itemStack   the item stack with that the check is processed
	 * @return              The free slot count if the given item stack will be added. 
	 */
	public static int getFreeSlotCountAfterAdding(@NotNull Inventory inventory, @NotNull ItemStack itemStack)
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
		
		if (openAmount > 0 && freeItemStackSpace > 0)
		{
			freeItemStackSpace--;
		}		
		return freeItemStackSpace;
	}
	
	public static int addToInventorySlots(@NotNull Inventory inventory, @NotNull List<Integer> slots, @Nullable ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}
		int openAmount = itemStack.getAmount();
		
		slots.sort(new SlotItemValueComparator(inventory, itemStack));
		
		for (int i = 0; i < slots.size() && openAmount > 0; i++)
		{
			final ItemStack currentItem = inventory.getItem(slots.get(i));
			
			if (currentItem == null || currentItem.getType() == Material.AIR)
			{
				final ItemStack copyItem = itemStack.clone();
				
				copyItem.setAmount(openAmount);
				inventory.setItem(slots.get(i), copyItem);
				openAmount = 0;
				break;
			}
			final int currentAmount = currentItem.getAmount();
			final int maxStackSize = currentItem.getMaxStackSize();
			
			Bukkit.getConsoleSender().sendMessage("CURRENT AMOUNT : " + currentAmount);
			Bukkit.getConsoleSender().sendMessage("MAX AMOUNT : " + maxStackSize);
			
			if (currentAmount < maxStackSize && currentItem.isSimilar(itemStack))
			{
				int possibleAmount = maxStackSize - currentAmount;
				
				Bukkit.getConsoleSender().sendMessage("POSSIBLE AMOUNT : " + possibleAmount);
				
				if (openAmount < possibleAmount)
				{
					currentItem.setAmount(currentAmount + openAmount);
					openAmount = 0;
				}
				else
				{
					currentItem.setAmount(maxStackSize);
					openAmount -= possibleAmount;
				}
			}
		}
		
		return openAmount;
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
			   inventoryAction == InventoryAction.PICKUP_ONE || inventoryAction == InventoryAction.PICKUP_SOME;
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
	 * 
	 * 
	 * @param   view
	 * @return
	 */
	public static boolean isInternalCraftView(InventoryView view)
	{
		return view.getTopInventory().getType() == InventoryType.CRAFTING &&
			   view.getBottomInventory().getType() == InventoryType.PLAYER &&
			   view.countSlots() == INV_SIZE_INTERNAL;
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
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T extends MenuHolder> T getMenuHolder(@Nullable Inventory inventory, Class<T> holderClass)
	{
		if (inventory == null)
		{
			return null;
		}
		final InventoryHolder inventoryHolder = inventory.getHolder(); 
		
		if (inventoryHolder != null && holderClass.isInstance(inventoryHolder))
		{
			return (T) inventoryHolder;
		}
		return null;
	}
	
	/**
	 * Gets a holder from the given inventory.
	 * If nothing can be obtained null will be returned.
	 * 
	 * @param   inventory     the inventory to get the holder from
	 * @return                The holder of the given inventory.
	 */
	@Nullable
	public static InventoryHolder getHolder(@Nullable Inventory inventory)
	{
		return inventory != null ? inventory.getHolder() : null;
	}
	
	/**
	 * Calculates and gets the slot from the given row and column.
	 * If row or column are out of bounds the maximal value will be used.
	 * 
	 * @param   inventorySize   the inventory size to define maximal rows
	 * @param   row             the row to calculate the slot from
	 * @param   column          the column to calculate the slot from
	 * @return                  The calculated slot.
	 */
	public static int getSlotFromRowColumn(int inventorySize, int row, int column)
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
		return (row * ROW_LENGTH) + column;
	}
	
	/**
	 * Calculates and gets the row and column from the given slot.
	 * 
	 * @param   slot   the slot to calculate the row and column from
	 * @return         The calculated row and column as pair.
	 */
	@NotNull
	public static Pair<Integer, Integer> getRowColumnFromSlot(int slot)
	{
		int row = slot / ROW_LENGTH;
		int column = slot % ROW_LENGTH;
		
		return new Pair<>(row, column); 
	}

	/**
	 * Checks if the player represented from the unique id is a viewer in the specified inventory.
	 * 
	 * @param   inventory   the inventory to check the viewer from
	 * @param   uuid        the unique id from the player to check
	 * @return              The check result.
	 */
	public static boolean isViewer(@NotNull Inventory inventory, @Nullable UUID uuid)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");
		
		if (uuid != null)
		{
			final Player player = Bukkit.getPlayer(uuid);
			
			return isViewer(inventory, player);
		}
		return false;
	}

	/**
	 * Checks if the given player is a viewer in the specified inventory.
	 * 
	 * @param   inventory   the inventory to check the viewer from
	 * @param   player      the player to check
	 * @return              The check result.
	 */
	public static boolean isViewer(@NotNull Inventory inventory, @Nullable HumanEntity human)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");

		return human != null && inventory.getViewers().contains(human);
	}
}
