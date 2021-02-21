package huff.lib.events;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerSignInputEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerSignInputEvent(@NotNull Player player, @NotNull String[] lines)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) lines, "The lines cannot be null.");
		
		this.player = player;
		this.lines = lines;
	}
	
	private final Player player;
	private final String[] lines;
	
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
}
