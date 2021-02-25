package huff.lib.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.PermissionHelper;
import huff.lib.various.LibConfig;
import huff.lib.various.LibMessage;
import huff.lib.various.structures.StringPair;
import net.luckperms.api.LuckPerms;

/**
 * A listener class that handles the appearance of the player chat.
 */
public class ChatListener implements Listener 
{
	/**
	 * @param    displayLuckPerms   determines whether a prefix is shown in chat
	 * @param    displayWorld       determines whether the current world is shown in chat
	 */
	public ChatListener(boolean displayLuckPerms, boolean displayWorld)
	{
		if (displayLuckPerms)
		{
			luckPerms = PermissionHelper.getLuckPerms();
		}
		this.displayWorld = displayWorld;
	}
	
	/**
	 * Chat configurations will be loaded from the lib config.
	 */
	public ChatListener()
	{
		this( LibConfig.CHAT_DISPLAYLUCKPERMS.getValue(), LibConfig.CHAT_DISPLAYWORLD.getValue());
	}
	
	private LuckPerms luckPerms;
	private boolean displayWorld;
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		final StringBuilder formatBuilder = new StringBuilder();
		
		formatBuilder.append(LibMessage.CHAT_MESSAGE.getValue(
				new StringPair("world", displayWorld ? player.getWorld().getName() + " §8×§7" : ""),
				new StringPair("userprefix", addPrefix(player)),
				new StringPair("user", "%1$s"),
				new StringPair("text", "%2$s")));
		
		event.setFormat(formatBuilder.toString());
	}
	
	@NotNull
	private String addPrefix(Player player)
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
