package huff.lib.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.JavaHelper;
import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import net.luckperms.api.LuckPerms;

/**
 * A listener class that handles the appearance of the player chat.
 * Also is splits the chat in a global and a area chat.
 */
public class AreaChatListener implements Listener
{
	/**
	 * @param    plugin               the java plugin instance
	 * @param    withLuckPerms        determines whether a prefix is shown in chat
	 * @param    globalChatCooldown   defines the cooldown for the global chat in milliseconds
	 * @param    areaChatRange        a bounding box that defines the chat range in all directions
	 */
	public AreaChatListener(@NotNull JavaPlugin plugin, boolean withLuckPerms, @Nullable Integer globalChatCooldown, @Nullable BoundingBox areaChatRange)
	{
		Validate.notNull((Object) plugin, "The plugin instance cannot be null.");
		
		if (withLuckPerms)
		{
			luckPerms = PermissionHelper.getLuckPerms();
		}	
		this.plugin = plugin;
		this.globalChatCooldown = globalChatCooldown != null ? globalChatCooldown.intValue() : JavaHelper.getSecondsInMillis(3);
		this.areaChatRange = areaChatRange != null ? areaChatRange : new BoundingBox(20, 10, 20, -20, -10, -20);
	}
	
	private final Map<UUID, Long> cooldownPlayer = new HashMap<>();
	private final JavaPlugin plugin;
	private final int globalChatCooldown;
	private final BoundingBox areaChatRange;
	private LuckPerms luckPerms;
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final String message = event.getMessage();
		final String globalMessage = removeGlobalFlag(message);
		final boolean isGlobalMessgae = globalMessage != null;		
		final long currentCooldown = JavaHelper.getValueOrDefault(cooldownPlayer.get(uuid), (long) 0) - (System.currentTimeMillis() - globalChatCooldown);
		
		if (isGlobalMessgae && currentCooldown > 0)
		{
			player.sendMessage(MessageHelper.PREFIX_HUFF + "Du musst noch " + (int) Math.ceil((double) currentCooldown / JavaHelper.SECOND_IN_MILLIS) + " Sekunde(n) warten.");
			event.setCancelled(true);
			return;
		}
		final StringBuilder formatBuilder = new StringBuilder();
		
		cooldownPlayer.remove(uuid);
		formatBuilder.append("§8☰ "); 		
		formatBuilder.append(isGlobalMessgae ? "§3Botschaft §8×§7 " : "§9Umgebung §8×§7 ");
		formatBuilder.append(addPrefix(player));
		formatBuilder.append("%1$s §8»§7 %2$s");
		
		if (isGlobalMessgae)
		{
			cooldownPlayer.put(uuid, System.currentTimeMillis());
			event.setMessage(globalMessage);
			event.setFormat(formatBuilder.toString());
		}
		else
		{
			final String finalMessage = String.format(formatBuilder.toString(), player.getName(), message);
			
			Bukkit.getScheduler().runTask(plugin, () ->
			{
				for (Entity nearbyEntity : player.getWorld().getNearbyEntities(areaChatRange))
				{
					if (nearbyEntity instanceof Player)
					{
						((Player) nearbyEntity).sendMessage(finalMessage);
					}
				}
				player.sendMessage(finalMessage);
			});
			event.setCancelled(true);
		}
	}
	
	private @NotNull String addPrefix(@NotNull Player player)
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
	
	private @Nullable String removeGlobalFlag(@NotNull String message)
	{
		final String trimmedMessage = message.trim();
		
		if (trimmedMessage.toLowerCase().startsWith("@all"))
		{
			return trimmedMessage.substring(4, trimmedMessage.length()).trim();
		}
		else if (trimmedMessage.toLowerCase().startsWith("@global"))
		{
			return trimmedMessage.substring(7, trimmedMessage.length()).trim();
		}
		else if (trimmedMessage.startsWith("!"))
		{
			return trimmedMessage.substring(1, trimmedMessage.length()).trim();
		}       
		return null;
	}
}
