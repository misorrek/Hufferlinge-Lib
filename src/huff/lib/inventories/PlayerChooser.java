package huff.lib.inventories;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.InventoryHelper;
import huff.lib.helper.NMSHelper;
import huff.lib.various.ExtendableInventory;

public class PlayerChooser extends ExtendableInventory
{
	private static final int PLAYER_PER_SITE = 28;	
	private static final String NBTTAG_UUID = "uuid";
	
	public PlayerChooser(@NotNull List<UUID> players, @Nullable String title, boolean isBackPossible)
	{
		super(null, 54, title != null ? title : "§7» §9Personenauswahl");
		
		Validate.notNull((Object) players, "The players-list cannot be null.");
		
		this.players = players;
		this.maxSite = (int) Math.ceil((double) players.size() / PLAYER_PER_SITE);
		
		initInventory(isBackPossible);
		setSiteFunction();
		setPlayers();
	}

	private final List<UUID> players;
	private final int maxSite;
	
	private int site = 0;
	
	public @Nullable UUID handleEvent(@NotNull ItemStack currentItem)
	{
     	if (currentItem.getType() == Material.PLAYER_HEAD)
     	{		 
			return UUID.fromString(NMSHelper.getTagFromItemStack(currentItem, NBTTAG_UUID));
     	}
		else if (currentItem.equals(this.inventory.getItem(48)))
		{
			changeSite(false);
		}
		else if (currentItem.equals(this.inventory.getItem(50)))
		{
			changeSite(true);
		}
     	return null;
	}
	
	private void initInventory(boolean isBackPossible)
	{
		final ItemStack borderItem = InventoryHelper.getBorderItem();
		
		//FIRST
		this.inventory.setItem(0, borderItem);
		this.inventory.setItem(1, borderItem);
		this.inventory.setItem(2, borderItem);
		this.inventory.setItem(3, borderItem);
		this.inventory.setItem(4, borderItem);
		this.inventory.setItem(5, borderItem);
		this.inventory.setItem(6, borderItem);
		this.inventory.setItem(7, borderItem);
		this.inventory.setItem(8, borderItem);
		//SECOND
		this.inventory.setItem(9, borderItem);
		this.inventory.setItem(17, borderItem);
		//THIRD
		this.inventory.setItem(18, borderItem);
		this.inventory.setItem(26, borderItem);
		//FORTH
		this.inventory.setItem(27, borderItem);
		this.inventory.setItem(35, borderItem);
		//FIFTH
		this.inventory.setItem(36, borderItem);
		this.inventory.setItem(44, borderItem);
		//SIXTH
		this.inventory.setItem(45, borderItem);
		this.inventory.setItem(46, borderItem);
		this.inventory.setItem(47, borderItem);

		this.inventory.setItem(49, InventoryHelper.getItemWithMeta(Material.BLACK_STAINED_GLASS_PANE, "§7» Seite «"));

		this.inventory.setItem(51, borderItem);
		this.inventory.setItem(52, borderItem);
		this.inventory.setItem(53, isBackPossible ? InventoryHelper.getBackItem() : InventoryHelper.getCloseItem());
	}
	
	private void setSiteFunction()
	{
		final ItemStack borderItem = InventoryHelper.getBorderItem();
		
		if (site > 0)
		{
			this.inventory.setItem(48, InventoryHelper.getItemWithMeta(Material.BLUE_STAINED_GLASS_PANE, "§7« §9Vorherige Seite"));
		}
		else
		{
			this.inventory.setItem(48, borderItem);
		}
		
		if (site != maxSite)
		{
			this.inventory.setItem(50, InventoryHelper.getItemWithMeta(Material.BLUE_STAINED_GLASS_PANE, "§7» §9Nächste Seite"));
		}
		else
		{
			this.inventory.setItem(50, borderItem);
		}
		this.inventory.getItem(49).setAmount(site + 1);
	}
	
	private void setPlayers()
	{
		final int startIndex = site * PLAYER_PER_SITE;
		final int maxIndex = startIndex + PLAYER_PER_SITE;
		
		for (int i = startIndex; i < players.size() && i < maxIndex; i++)
		{
			final OfflinePlayer player = Bukkit.getOfflinePlayer(players.get(i));
			
			this.inventory.addItem(NMSHelper.getTaggedItemStack(InventoryHelper.getSkullWithMeta(player, "§9" + player.getName()), NBTTAG_UUID, player.getUniqueId().toString()));	
		}
	}
	
	private void clearPlayers()
	{
		for (int i = 10; i <= 43; i++)
		{
			if (i % 9 == 0 || (i - 1) % 9 == 0)
			{
				continue;
			}			
			this.inventory.setItem(i, null);
		}
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
