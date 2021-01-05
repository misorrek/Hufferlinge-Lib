package huff.lib.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.PermissionHelper;
import net.luckperms.api.LuckPerms;

/**
 * A listener class that handles the appearance of the player chat.
 */
public class ChatListener implements Listener 
{
	public ChatListener(boolean withLuckPerms, boolean withWorldDisplay)
	{
		if (withLuckPerms)
		{
			luckPerms = PermissionHelper.getLuckPerms();
		}
		this.withWorldDisplay = withWorldDisplay;
	}
	
	private LuckPerms luckPerms;
	private boolean withWorldDisplay = false;
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		final StringBuilder formatBuilder = new StringBuilder();
		
		formatBuilder.append("§8☰§7 ");
		formatBuilder.append(withWorldDisplay ? player.getWorld().getName() + " §8×§7 " : "");
		formatBuilder.append(addPrefix(player));
		formatBuilder.append("%1$s §8»§7 %2$s");
		event.setFormat(formatBuilder.toString());
	}
	
	private @NotNull String addPrefix(Player player)
	{
		if (luckPerms != null) 
		{
		    final String primaryPlayerPrefix = PermissionHelper.getPrimaryPlayerPrefix(luckPerms, player);
		    
		    if (primaryPlayerPrefix != null && !primaryPlayerPrefix.isEmpty())
		    {
		    	return ChatColor.translateAlternateColorCodes('&', primaryPlayerPrefix);
		    }
		}
		return "";
	}
}
