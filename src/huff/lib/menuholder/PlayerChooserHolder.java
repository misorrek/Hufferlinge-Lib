package huff.lib.menuholder;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.InventoryHelper;
import huff.lib.helper.ItemHelper;
import huff.lib.helper.MessageHelper;
import huff.lib.helper.StringHelper;
import huff.lib.various.Action;
import huff.lib.various.LibMessage;

/**
 * A menu holder class that contains a menu to choose a entry from a given list of UUID's.
 * The inventory has the possibility to display several pages.
 * The chosen entry will returned via the given action.
 */
public class PlayerChooserHolder extends MenuHolder
{
	private static final int MIN_SIZE = InventoryHelper.INV_SIZE_3;
	private static final int MAX_SIZE = InventoryHelper.INV_SIZE_6;
	private static final int START_PAGE = 1;
	
	/**
	 * @param   plugin         the java plugin instance
	 * @param   players        the list of UUID's representing human entities
	 * @param   size           the size the menu inventory should have
	 * @param   title          a optional title for the menu inventory
	 * @param   menuExitType   the menu exit behavior
	 * @param   chooseAction   the action called when a entry were chosen
	 */
	public PlayerChooserHolder(@NotNull JavaPlugin plugin, @NotNull List<UUID> players, int size, @Nullable String title, MenuExitType menuExitType, Action chooseAction)
	{
		super("playerchooser", checkSize(size), title != null ? title : LibMessage.TITLE_PLAYERCHOOSER.getMessage(), menuExitType);
		
		Validate.notNull((Object) plugin, "The plugin instance cannot be null.");
		Validate.notNull((Object) players, "The player list cannot be null.");
		Validate.notNull((Object) chooseAction, "The choose action cannot be null.");
		
		this.plugin = plugin;
		this.players = players;
		this.chooseAction = chooseAction;
		this.entriesPerPage = ((super.getInventory().getSize() / InventoryHelper.ROW_LENGTH) - 2) * InventoryHelper.ROW_LENGTH - 2;
		this.lastPage = (int) Math.ceil((double) players.size() / entriesPerPage);
		
		initInventory();
		setPageFunctions();
		setPlayers();
	}

	private final JavaPlugin plugin;
	private final List<UUID> players;
	private final Action chooseAction;
	private final int entriesPerPage;
	private final int lastPage;
	
	private int page = START_PAGE;
	
	@Override
	public boolean handleClick(@NotNull InventoryClickEvent event)
	{
		Validate.notNull((Object) event, "The inventory click event cannot be null.");
		
		final ItemStack currentItem = event.getCurrentItem();
		final int inventorySize = super.getInventory().getSize();
		
		if (ItemHelper.hasMeta(currentItem))
		{
			if (currentItem.getType() == Material.PLAYER_HEAD)
	     	{		 
	     		final OfflinePlayer currentOwningPlayer = ((SkullMeta) currentItem.getItemMeta()).getOwningPlayer();
	     		
	     		if (currentOwningPlayer != null)
	     		{
	     			chooseAction.execute(currentOwningPlayer.getUniqueId());
	     		}
	     	}
			else if (event.getSlot() == InventoryHelper.getSlotFromRowColumn(inventorySize, InventoryHelper.getLastLine(inventorySize), 4)) 
			{
				changePage(false);
			}
			else if (event.getSlot() == InventoryHelper.getSlotFromRowColumn(inventorySize, InventoryHelper.getLastLine(inventorySize), 6))
			{
				changePage(true);
			}
		}
     	return true;
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

	private void initInventory()
	{				
		InventoryHelper.setBorder(super.getInventory(), InventoryHelper.getBorderItem());
		super.setMenuExitItem();
	}
	
	private void setPageFunctions()
	{
		final ItemStack borderItem = InventoryHelper.getBorderItem();	
		
		InventoryHelper.setItem(super.getInventory(), InventoryHelper.LAST_ROW, 5, ItemHelper.getItemWithMeta(Material.WHITE_STAINED_GLASS_PANE, 
				                                                                                             StringHelper.build("§7» Seite §9", Integer.toString(page), "§7 «")));
		
		if (page > START_PAGE)
		{
			InventoryHelper.setItem(super.getInventory(), InventoryHelper.LAST_ROW, 4, ItemHelper.getItemWithMeta(Material.BLUE_STAINED_GLASS_PANE, "§7« §9Vorherige Seite"));
		}
		else
		{
			InventoryHelper.setItem(super.getInventory(), InventoryHelper.LAST_ROW, 4, borderItem);
		}
		
		if (page < lastPage)
		{
			InventoryHelper.setItem(super.getInventory(), InventoryHelper.LAST_ROW, 6, ItemHelper.getItemWithMeta(Material.BLUE_STAINED_GLASS_PANE, "§7» §9Nächste Seite"));
		}
		else
		{
			InventoryHelper.setItem(super.getInventory(), InventoryHelper.LAST_ROW, 6, borderItem);
		}
		final ItemStack pageItem = InventoryHelper.getItem(super.getInventory(), InventoryHelper.LAST_ROW, 5);
		
		if (pageItem != null)
		{
			pageItem.setAmount(page); 
		}
	}
	
	private void setPlayers()
	{	
		final int startIndex = (page - 1) * entriesPerPage;
		final int maxIndex = startIndex + entriesPerPage;
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> 
		{
			for (int i = startIndex; i < players.size() && i < maxIndex; i++)
			{			
				final OfflinePlayer player = Bukkit.getOfflinePlayer(players.get(i));

				super.getInventory().addItem(ItemHelper.getSkullWithMeta(player, MessageHelper.getHighlighted(player.getName(), false, false)));
			}
		});
	}

	private void clearPlayers()
	{
		InventoryHelper.setFill(super.getInventory(), null, false);
	}
	
	private void changePage(boolean increase)
	{
		if (increase && page < lastPage)
		{
			page++;
		}
		else if (!increase && page > START_PAGE)
		{
			page--;
		}	
		setPageFunctions();
		clearPlayers();
		setPlayers();
	}
}
