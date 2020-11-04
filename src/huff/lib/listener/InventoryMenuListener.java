package huff.lib.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import huff.lib.helper.InventoryHelper;
import huff.lib.various.ExpandableInventory;

public class InventoryMenuListener implements Listener
{
	private HashMap<UUID, List<Inventory>> lastInventories = new HashMap<>();
	private List<UUID> goneBack = new ArrayList<>();
	
	public void onInventoryMenuClick(InventoryClickEvent event)
	{
		if (event.getCurrentItem() == null)
		{
			return;
		}
		final InventoryView view = event.getView();
		final String currentItemName = event.getCurrentItem().getItemMeta().getDisplayName();
		
		if (currentItemName.equals(InventoryHelper.ITEM_CLOSE))
		{
			view.close();
		} 
		else if (currentItemName.equals(InventoryHelper.ITEM_BACK) || currentItemName.equals(InventoryHelper.ITEM_ABORT))
		{
			final HumanEntity human = event.getView().getPlayer();
			
			if (lastInventories.containsKey(human.getUniqueId()))
			{
				final List<Inventory> inventories = lastInventories.get(human.getUniqueId());
				
				if (!inventories.isEmpty())
				{
					final int lastIndex = inventories.size() - 1;
					
					goneBack.add(human.getUniqueId());
					human.closeInventory();
					human.openInventory(inventories.get(lastIndex));
					inventories.remove(lastIndex);
				}
			}
		}
		
	}

	public void onInventoryMenuOpen(InventoryOpenEvent event)
	{
		
	}
	
	public void onInventoryMenuClose(InventoryCloseEvent event)
	{
		if (!(event.getInventory().getHolder() instanceof ExpandableInventory))
		{
			return;
		}
		final UUID uuid = event.getPlayer().getUniqueId();
		
		if (goneBack.contains(uuid))
		{
			goneBack.remove(uuid);
			return;
		}
		
		if (lastInventories.containsKey(uuid))
		{
			lastInventories.get(uuid).add(event.getInventory());
		}
		else
		{
			final ArrayList<Inventory> inventories = new ArrayList<>();
			
			inventories.add(event.getInventory());
			lastInventories.put(uuid, inventories);
		}
	}
}
