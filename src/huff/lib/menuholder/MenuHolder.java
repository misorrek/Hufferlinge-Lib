package huff.lib.menuholder;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.InventoryHelper;

public abstract class MenuHolder implements InventoryHolder 
{
	public MenuHolder(@NotNull String identifier, int size, @Nullable String title, MenuExitType menuExitType)
	{
		this.menuExitType = menuExitType;
		this.identifier = identifier;
		this.inventory = Bukkit.createInventory(this, size, title != null ? title : "");
		resetReturnable();
		resetForwarding();
	}	
	
	public MenuHolder(@NotNull String identifier, InventoryType type, @Nullable String title, MenuExitType menuExitType)
	{
		this.menuExitType = menuExitType;
		this.identifier = identifier;
		this.inventory = Bukkit.createInventory(this, type, title != null ? title : "");
		resetReturnable();
		resetForwarding();
	}
	
	private final MenuExitType menuExitType;
	private final String identifier;
	private final Inventory inventory;
	
	private boolean isReturnable;
	private boolean isForwarding;
	
	public static void open(@NotNull HumanEntity human, @NotNull MenuHolder menuHolder)
	{
		open(human, menuHolder, false);
	}
	
	public static void open(@NotNull HumanEntity human, @NotNull MenuHolder menuHolder, boolean withoutClose)
	{
		Validate.notNull((Object) menuHolder, "The menu holder cannot be null.");

		if (!withoutClose)
		{
			closeInventory(human, false, true);
		}
		menuHolder.resetReturnable();
		menuHolder.resetForwarding();
		human.openInventory(menuHolder.getInventory());
	}
	
	public static void back(@NotNull HumanEntity human)
	{
		closeInventory(human, true, false);
	}
	
	public static void close(@NotNull HumanEntity human)
	{
		closeInventory(human, false, false);
	}
	
	private static void closeInventory(@NotNull HumanEntity human, boolean isReturnable, boolean isForwarding)
	{
		Validate.notNull((Object) human, "The human cannot be null.");
		
		final InventoryHolder inventoryHolder = human.getOpenInventory().getTopInventory().getHolder();
		
		if (inventoryHolder instanceof MenuHolder)
		{
			((MenuHolder) inventoryHolder).setForwarding(isForwarding);
			((MenuHolder) inventoryHolder).setReturnable(isReturnable);
		}
		human.closeInventory();
	}

	public abstract boolean handleClick(@NotNull InventoryClickEvent event);
	
	public boolean handleDrag(@NotNull InventoryDragEvent event) { return false; }
	
	public void handleClose(@NotNull InventoryCloseEvent event) { }
	
	@Override
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public boolean equalsIdentifier(MenuHolder menuInventoryHolder)
	{
		return identifier.equals(menuInventoryHolder.identifier);
	}
	
	public MenuExitType getMenuExitType()
	{
		return menuExitType;
	}
	
	public boolean isReturnable()
	{
		return isReturnable;
	}
	
	public void resetReturnable()
	{
		isReturnable = menuExitType.isReturnType();
	}
	
	public void setReturnable(boolean isReturnable)
	{
		this.isReturnable = isReturnable;
	}
	
	public boolean isForwarding()
	{	
		return isForwarding;
	}
	
	public void resetForwarding()
	{
		isForwarding = false;
	}
	
	public void setForwarding(boolean isForwarding)
	{
		this.isForwarding = isForwarding;
	}

	protected void setMenuExitItem()
	{
		switch(getMenuExitType())
		{
		case CLOSE:
			InventoryHelper.setItem(this.getInventory(), 
					                InventoryHelper.getLastLine(this.getInventory().getSize()), 5, 
					                InventoryHelper.getCloseItem());
			break;
		case BACK:
			InventoryHelper.setItem(this.getInventory(), 
	                InventoryHelper.getLastLine(this.getInventory().getSize()), 9, 
	                InventoryHelper.getBackItem());
            break;
		case ABORT:
			InventoryHelper.setItem(this.getInventory(), 
	                InventoryHelper.getLastLine(this.getInventory().getSize()), 9, 
	                InventoryHelper.getAbortItem());
            break;
		default:
			break;
		}
	}
}
