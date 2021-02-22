package huff.lib.events;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.menuholder.MenuHolder;

/**
 * Event that is called when a player finished input via custom sign editor.
 */
public class PlayerSignInputEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerSignInputEvent(@NotNull Player player, @NotNull String[] lines, @Nullable MenuHolder menuHolder)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) lines, "The lines cannot be null.");
		
		this.player = player;
		this.lines = lines;
		this.menuHolder = menuHolder;
	}
	
	private final Player player;
	private final String[] lines;
	private final MenuHolder menuHolder;
	
	@NotNull
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	@Override
	@NotNull
	public HandlerList getHandlers()
	{
		return getHandlerList();
	}
	
	@NotNull
	public Player getPlayer()
	{
		return player;
	}
	
	@NotNull
	public String[] getLines()
	{
		return lines;
	}
	
	@Nullable
	public MenuHolder getMenuHolder()
	{
		return menuHolder;
	}
	
	public boolean isMenuSignInput()
	{
		return menuHolder != null;
	}
}
