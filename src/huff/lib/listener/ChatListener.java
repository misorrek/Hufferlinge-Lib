package huff.lib.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import huff.lib.helper.PermissionHelper;
import net.luckperms.api.LuckPerms;

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
		Player player = event.getPlayer();
		StringBuilder formatBuilder = new StringBuilder();
		
		formatBuilder.append("§8☰§7 ");
		
		if (withWorldDisplay)
		{
			formatBuilder.append(player.getWorld().getName() + " §8×§7 ");
		}
		
		if (luckPerms != null) 
		{
		    String primaryPlayerPrefix = PermissionHelper.getPrimaryPlayerPrefix(luckPerms, player);
		    
		    if (primaryPlayerPrefix != null && !primaryPlayerPrefix.isEmpty())
		    {
		    	formatBuilder.append(ChatColor.translateAlternateColorCodes('&', primaryPlayerPrefix));
		    }
		}
		formatBuilder.append("%1$s §8»§7 %2$s");
		event.setFormat(formatBuilder.toString());
	}
}
