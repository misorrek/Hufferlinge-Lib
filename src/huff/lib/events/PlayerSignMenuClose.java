package huff.lib.events;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerSignMenuClose extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerSignMenuClose(@NotNull Player player)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		this.player = player;
	}
	
	private final Player player;
	
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
}
