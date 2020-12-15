package huff.lib.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import huff.lib.helper.InventoryHelper;
import huff.lib.various.MenuHolder;

public class MenuInventoryListener implements Listener
{
	private HashMap<UUID, List<Inventory>> lastInventories = new HashMap<>();
	private List<UUID> goneBack = new ArrayList<>();
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onInventoryMenuClick(InventoryClickEvent event)
	{
		if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null)
		{
			return;
		}
		final InventoryView view = event.getView();
		final String currentItemName = event.getCurrentItem().getItemMeta().getDisplayName();
		
		//Bukkit.getConsoleSender().sendMessage("CurrentItem : " + event.getCurrentItem().toString());
		
		if (currentItemName.equals(InventoryHelper.ITEM_CLOSE))
		{
			//Bukkit.getConsoleSender().sendMessage("CloseItem : TRUE");
			
			final UUID uuid = event.getView().getPlayer().getUniqueId();
					
			goneBack.add(uuid);
			
			if (lastInventories.containsKey(uuid))
			{
				lastInventories.remove(uuid);
			}
			view.close();
		} 
		else if (currentItemName.equals(InventoryHelper.ITEM_BACK) || currentItemName.equals(InventoryHelper.ITEM_ABORT))
		{
			//Bukkit.getConsoleSender().sendMessage("BackItem : TRUE");
			
			final HumanEntity human = event.getView().getPlayer();
			
			if (lastInventories.containsKey(human.getUniqueId()))
			{
				//Bukkit.getConsoleSender().sendMessage("ContainsLastInv : TRUE");
				
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
	
	@EventHandler
	public void onInventoryMenuClose(InventoryCloseEvent event)
	{
		final UUID uuid = event.getPlayer().getUniqueId();
		
		//TODO If contains back/abort item use that!
		
		if (!(event.getInventory().getHolder() instanceof MenuHolder))
		{
			if (lastInventories.containsKey(uuid))
			{
				lastInventories.remove(uuid);
			}
			return;
		}
		//Bukkit.getConsoleSender().sendMessage("OpenInventory : " + event.getPlayer().getOpenInventory().countSlots());
		//Bukkit.getConsoleSender().sendMessage("MenuInventoryHolder : TRUE");
			
		if (goneBack.contains(uuid))
		{
			//Bukkit.getConsoleSender().sendMessage("ContainsGoneBack : TRUE");
			goneBack.remove(uuid);
			return;
		}
		
		if (lastInventories.containsKey(uuid))
		{
			//Bukkit.getConsoleSender().sendMessage("AddToList : TRUE");
			
			final List<Inventory> inventories = lastInventories.get(uuid);
			
			for (int i = 0; i < inventories.size(); i++)
			{
				if (((MenuHolder) inventories.get(i).getHolder()).equalsIdentifier((MenuHolder) event.getInventory().getHolder()))
				{
					inventories.set(i, event.getInventory());
					return;
				}
			}
			inventories.add(event.getInventory());
		}
		else
		{
			//Bukkit.getConsoleSender().sendMessage("NewList : TRUE");
			
			final ArrayList<Inventory> inventories = new ArrayList<>();
			
			inventories.add(event.getInventory());
			lastInventories.put(uuid, inventories);
		}
	}
}
