package huff.lib.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.BoundingBox;

import huff.lib.helper.PermissionHelper;
import net.luckperms.api.LuckPerms;

/**
 * A listener class that handles the appearance of the player chat.
 * Also is splits the chat in a global and a area chat.
 */
public class AreaChatListener
{
	public AreaChatListener(boolean withLuckPerms)
	{
		if (withLuckPerms)
		{
			luckPerms = PermissionHelper.getLuckPerms();
		}
	}
	
	private LuckPerms luckPerms;
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		final String message = event.getMessage();
		final boolean isGlobalMessgae = message.trim().toLowerCase().startsWith("@all") || message.trim().toLowerCase().startsWith("@global") || 
			                        message.trim().startsWith("!");
		final StringBuilder formatBuilder = new StringBuilder();
		
		formatBuilder.append("§8☰ ");
		
		if (isGlobalMessgae)
		{
			formatBuilder.append("§3Botschaft §8×§7 ");
		}
		else
		{
			formatBuilder.append("§9Umgebung §8×§7 ");
		}
		
		if (luckPerms != null) 
		{
		    final String primaryPlayerPrefix = PermissionHelper.getPrimaryPlayerPrefix(luckPerms, player);
		    
		    if (primaryPlayerPrefix != null && !primaryPlayerPrefix.isEmpty())
		    {
		    	formatBuilder.append(ChatColor.translateAlternateColorCodes('&', primaryPlayerPrefix));
		    }
		}	
		formatBuilder.append("%1$s §8»§7 %2$s");
		
		if (isGlobalMessgae)
		{
			event.setFormat(formatBuilder.toString());
		}
		else
		{
			final BoundingBox areaChatBox = new BoundingBox(20, 10, 20, -20, -10, -20);
			final String finalMessage = String.format(formatBuilder.toString(), player.getName(), message);
			
			for (Entity nearbyEntity : player.getWorld().getNearbyEntities(areaChatBox))
			{
				if (nearbyEntity instanceof Player)
				{
					((Player) nearbyEntity).sendMessage(finalMessage);
				}
			}
			player.sendMessage(finalMessage);
			event.setCancelled(true);
		}
	}
}
