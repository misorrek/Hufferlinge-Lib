package huff.lib.various;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuInventoryHolder implements InventoryHolder 
{
	public MenuInventoryHolder(@NotNull String identifier, int size, @Nullable String title)
	{
		this.identifier = identifier;
		this.inventory = Bukkit.createInventory(this, size, title != null ? title : "");
	}	
	
	public MenuInventoryHolder(@NotNull String identifier, InventoryType type, @Nullable String title)
	{
		this.identifier = identifier;
		this.inventory = Bukkit.createInventory(this, type, title != null ? title : "");
	}
	
	private final String identifier;
	private final Inventory inventory;
	
	@Override
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public boolean equalsIdentifier(MenuInventoryHolder menuInventoryHolder)
	{
		return this.identifier.equals(menuInventoryHolder.identifier);
	}
}
