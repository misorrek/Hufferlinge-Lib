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
	/*
	@Override
	public int getSize()
	{
		return inventory.getSize();
	}

	@Override
	public int getMaxStackSize()
	{
		return inventory.getMaxStackSize();
	}

	@Override
	public void setMaxStackSize(int var1)
	{
		inventory.setMaxStackSize(var1);
	}

	@Override
	public ItemStack getItem(int var1)
	{
		return inventory.getItem(var1);
	}

	@Override
	public void setItem(int var1, ItemStack var2)
	{
		inventory.setItem(var1, var2);
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... var1) throws IllegalArgumentException
	{
		return inventory.addItem(var1);
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... var1) throws IllegalArgumentException
	{
		return inventory.removeItem(var1);
	}

	@Override
	public ItemStack[] getContents()
	{
		return inventory.getContents();
	}

	@Override
	public void setContents(ItemStack[] var1) throws IllegalArgumentException
	{
		inventory.setContents(var1);
	}

	@Override
	public ItemStack[] getStorageContents()
	{
		return inventory.getStorageContents();
	}

	@Override
	public void setStorageContents(ItemStack[] var1) throws IllegalArgumentException
	{
		inventory.setStorageContents(var1);
	}

	@Override
	public boolean contains(Material var1) throws IllegalArgumentException
	{
		return inventory.contains(var1);
	}

	@Override
	public boolean contains(ItemStack var1)
	{
		return inventory.contains(var1);
	}

	@Override
	public boolean contains(Material var1, int var2) throws IllegalArgumentException
	{
		return inventory.contains(var1, var2);
	}

	@Override
	public boolean contains(ItemStack var1, int var2)
	{
		return inventory.contains(var1, var2);
	}

	@Override
	public boolean containsAtLeast(ItemStack var1, int var2)
	{
		return inventory.containsAtLeast(var1, var2);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material var1) throws IllegalArgumentException
	{
		return inventory.all(var1);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack var1)
	{
		return inventory.all(var1);
	}

	@Override
	public int first(Material var1) throws IllegalArgumentException
	{
		return inventory.first(var1);
	}

	@Override
	public int first(ItemStack var1)
	{
		return inventory.first(var1);
	}

	@Override
	public int firstEmpty()
	{
		return inventory.firstEmpty();
	}

	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}

	@Override
	public void remove(Material var1) throws IllegalArgumentException
	{
		inventory.remove(var1);
	}

	@Override
	public void remove(ItemStack var1)
	{
		inventory.remove(var1);
	}

	@Override
	public void clear(int var1)
	{
		inventory.clear(var1);
	}

	@Override
	public void clear()
	{
		inventory.clear();
	}

	@Override
	public List<HumanEntity> getViewers()
	{
		return inventory.getViewers();
	}

	@Override
	public InventoryType getType()
	{
		return inventory.getType();
	}

	@Override
	public InventoryHolder getHolder()
	{
		return inventory.getHolder();
	}

	@Override
	public ListIterator<ItemStack> iterator()
	{
		return inventory.iterator();
	}

	@Override
	public ListIterator<ItemStack> iterator(int var1)
	{
		return inventory.iterator(var1);
	}

	@Override
	public Location getLocation()
	{
		return inventory.getLocation();
	}*/
}
