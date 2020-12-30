package huff.lib.various;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A comparer class to compare the item value of two slots in a inventory.
 * If the item in the current slot has a bigger value than the item in the next slot it will move up in the list.
 * Otherwise it will move down the list.
 */
public class SlotItemValueComparator implements Comparator<Integer>
{
	public SlotItemValueComparator(@NotNull Inventory inventory, @Nullable ItemStack itemStack)
	{
		Validate.notNull((Object) inventory, "The inventory cannot be null.");
		
		this.inventory = inventory;
		this.itemStack = itemStack;
	}
	
	final Inventory inventory;
	final ItemStack itemStack;
	
	@Override
	public int compare(@NotNull Integer currentSlot, @NotNull Integer nextSlot)
	{
		Validate.notNull((Object) currentSlot, "The current slot for comparison cannot be null.");
		Validate.notNull((Object) nextSlot, "The next slot for comparison cannot be null.");
		
		return Integer.compare(getItemValue(currentSlot), getItemValue(nextSlot));
	}
	
	private int getItemValue(int slot)
	{
		final ItemStack currentItem = inventory.getItem(slot);
		
		if (currentItem == null || currentItem.getType() == Material.AIR)
		{
			return 0; // MIDDLE VALUE
		}
		else if (currentItem.isSimilar(itemStack) && currentItem.getAmount() < currentItem.getMaxStackSize())
		{
			return -1; // HIGH VALUE
		}
		else
		{
			return 1; // LOW VALUE
		}
	}
}
