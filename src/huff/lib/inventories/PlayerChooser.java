package huff.lib.inventories;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.InventoryHelper;
import huff.lib.helper.ItemHelper;
import huff.lib.various.ExpandableInventory;

public class PlayerChooser extends ExpandableInventory
{
	private static final int MIN_SIZE = InventoryHelper.INV_SIZE_3;
	private static final int MAX_SIZE = InventoryHelper.INV_SIZE_6;
	
	public PlayerChooser(@NotNull List<UUID> players, int size, @Nullable String title, boolean isBackPossible)
	{
		super(null, checkSize(size), title != null ? title : "§7» §9Personenauswahl");
		
		Validate.notNull((Object) players, "The players-list cannot be null.");
		
		this.players = players;
		this.playersPerSite = ((this.getSize() % InventoryHelper.ROW_LENGTH) - 2) * InventoryHelper.ROW_LENGTH - 2;
		this.maxSite = (int) Math.ceil((double) players.size() / playersPerSite);
		
		initInventory(isBackPossible);
		setSiteFunction();
		setPlayers();
	}

	private final List<UUID> players;
	private final int playersPerSite;
	private final int maxSite;
	
	private int site = 0;
	
	public @Nullable UUID handleEvent(@NotNull ItemStack currentItem)
	{		
     	if (currentItem.getType() == Material.PLAYER_HEAD)
     	{		 
     		final OfflinePlayer currentOwningPlayer = ((SkullMeta) currentItem.getItemMeta()).getOwningPlayer();
     		
			return currentOwningPlayer != null ? currentOwningPlayer.getUniqueId() : null;
     	}
		else if (currentItem.equals(InventoryHelper.getItem(this, InventoryHelper.LAST_ROW, 4)))
		{
			changeSite(false);
		}
		else if (currentItem.equals(InventoryHelper.getItem(this, InventoryHelper.LAST_ROW, 6)))
		{
			changeSite(true);
		}
     	return null;
	}
	
	private static int checkSize(int size)
	{
		if (size < MIN_SIZE)
		{
			return MIN_SIZE;
		}
		
		if (size > MAX_SIZE)
		{
			return MAX_SIZE;
		}
		return size;
	}

	private void initInventory(boolean isBackPossible)
	{				
		InventoryHelper.setBorder(this, InventoryHelper.getBorderItem());
		InventoryHelper.setItem(this, InventoryHelper.LAST_ROW, 5, ItemHelper.getItemWithMeta(Material.BLACK_STAINED_GLASS_PANE, "§7» Seite «"));
		InventoryHelper.setItem(this, InventoryHelper.LAST_ROW, 9, isBackPossible ? InventoryHelper.getBackItem() : InventoryHelper.getCloseItem());
	}
	
	private void setSiteFunction()
	{
		final ItemStack borderItem = InventoryHelper.getBorderItem();	
		
		if (site > 0)
		{
			InventoryHelper.setItem(this, InventoryHelper.LAST_ROW, 4, ItemHelper.getItemWithMeta(Material.BLUE_STAINED_GLASS_PANE, "§7« §9Vorherige Seite"));
		}
		else
		{
			InventoryHelper.setItem(this, InventoryHelper.LAST_ROW, 4, borderItem);
		}
		
		if (site != maxSite)
		{
			InventoryHelper.setItem(this, InventoryHelper.LAST_ROW, 6, ItemHelper.getItemWithMeta(Material.BLUE_STAINED_GLASS_PANE, "§7» §9Nächste Seite"));
		}
		else
		{
			InventoryHelper.setItem(this, InventoryHelper.LAST_ROW, 6, borderItem);
		}
		final ItemStack siteItem = InventoryHelper.getItem(this, InventoryHelper.LAST_ROW, 5);
		
		if (siteItem != null)
		{
			siteItem.setAmount(site + 1); 
		}
	}
	
	private void setPlayers()
	{
		final int startIndex = site * playersPerSite;
		final int maxIndex = startIndex + playersPerSite;
		
		for (int i = startIndex; i < players.size() && i < maxIndex; i++)
		{
			final OfflinePlayer player = Bukkit.getOfflinePlayer(players.get(i));
			
			this.inventory.addItem(ItemHelper.getSkullWithMeta(player, "§9" + player.getName()));	
		}
	}
	
	private void clearPlayers()
	{
		InventoryHelper.setFill(this, null, false);
	}
	
	private void changeSite(boolean increase)
	{
		if (increase && site < maxSite)
		{
			site++;
		}
		else if (site > 0)
		{
			site--;
		}	
		setSiteFunction();
		clearPlayers();
		setPlayers();
	}
}
