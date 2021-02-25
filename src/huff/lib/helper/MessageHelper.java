package huff.lib.helper;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.various.LibMessage;

public class MessageHelper 
{
	private MessageHelper() { }

	public static final String PLACEHOLDER_HOUR = "%hour%";
	public static final String PLACEHOLDER_MINUTE = "%minute%";
      
	/**
	 * Makes the given string highlighted, therefore and behind a space and sets the standard text color again.
	 * 
	 * @param   content   the target string
	 * @return            The highlighted string.
	 */
	@NotNull
    public static String getHighlighted(@NotNull String content)
    {
    	return getHighlighted(content, true, true);
    }
    
	/**
	 * Makes the given string highlighted, optional therefore and/or behind a space and sets the standard text color again.
	 * 
	 * @param   content      the target string
	 * @param   spaceLeft    determines whether a space left will be added 
	 * @param   spaceRight   determines whether a space right will be added 
	 * @return               The highlighted string.
	 */
	@NotNull
    public static String getHighlighted(@NotNull String content, boolean spaceLeft, boolean spaceRight)
    {
    	Validate.notNull((Object) content, "The content cannot be null.");
    	
    	return (spaceLeft ? " §9" : "§9") + content + (spaceRight ? " §7" : "§7");
    }
    
	/**
	 * Sets the string in quotes, makes it highlighted, therefore and behind a space and sets the standard text color again.
	 * 
	 * @param   content   the target string
	 * @return            The quoted and highlighted string.
	 */
	@NotNull
    public static String getQuoted(@NotNull String content)
    {
    	return getQuoted(content, true, true);
    }
    
	/**
	 * Sets the string in quotes, makes it highlighted, optional therefore and/or behind a space and sets the standard text color again.
	 * 
	 * @param   content      the target string
	 * @param   spaceLeft    determines whether a space left will be added 
	 * @param   spaceRight   determines whether a space right will be added 
	 * @return               The quoted and highlighted string.
	 */
	@NotNull
    public static String getQuoted(@NotNull String content, boolean spaceLeft, boolean spaceRight)
    {
    	Validate.notNull((Object) content, "The content cannot be null.");
    	
    	return (spaceLeft ? " §9\"" : "§9\"") + content + (spaceRight ? "\"§7 " : "\"§7");
    }
    
	/**
	 * Sends the given message to the console with the in the message configuration defined prefix.
	 * 
	 * @param   message   the string representing the message to send
	 */
    public static void sendConsoleMessage(@NotNull String message)
    {
    	Validate.notNull((Object) message, "The message cannot be null.");
    	
    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('§', LibMessage.PREFIX_CONSOLE.getValue() + message));
    }
    
    /**
	 * Sends the given message to all players with the specified permission.
	 * 
	 * @param   permission   the permission that a player must have to receive the message
	 * @param   message      the string representing the message to send
	 */
    public static void sendPermssionMessage(@NotNull String permission, @NotNull String message) 
	{
    	sendPermssionMessage(permission, message, null);
	}
    
    /**
	 * Sends the given message to all players with the specified permission.
	 * 
	 * @param   permission       the permission that a player must have to receive the message
	 * @param   message          the string representing the message to send
	 * @param   excludedPlayer   a player that will be excluded whether he has the permission or not
	 */
    public static void sendPermssionMessage(@NotNull String permission, @NotNull String message, @Nullable String excludedPlayer) 
	{
    	Validate.notNull((Object) message, "The permission cannot be null.");
    	Validate.notNull((Object) message, "The message cannot be null.");
    	
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			if ((StringUtils.isNotEmpty(excludedPlayer) || !player.getName().equals(excludedPlayer)) && PermissionHelper.hasPlayerPermission(player, permission)) 
			{
				player.sendMessage(message);
			}
		}
	}
    
    /**
     * Gets the given world time (0 - 24000) formatted in hour and minute.
     * By default the time will be formatted to "%hour%:%minute%".
     * 
     * @param   time      the world time (0 - 24000)
     * @param   pattern   a optional pattern to create a custom format. (Available: %hour%, %minute%)
     * @return            The formatted time as string.
     */
    @NotNull
    public static String getTimeFormatted(int time, @Nullable String pattern)
    {
		final int maxTime = 24;
		final int hourAddition = 6;
		final int minuteMultiplier = 60;
		
		final int hourValue = ((int) (time * 0.001)) + hourAddition;
		final int hour = hourValue >= maxTime ? hourValue - maxTime : hourValue;
		final int minute = (time / JavaHelper.SECOND_IN_MILLIS) * minuteMultiplier;
		
		if (StringUtils.isNotEmpty(pattern) && StringHelper.contains(false, pattern, PLACEHOLDER_HOUR, PLACEHOLDER_MINUTE))
		{
			return pattern.replace(PLACEHOLDER_HOUR, Integer.toString(hour)).replace(PLACEHOLDER_MINUTE, Integer.toString(minute));
		}
		else
		{
			return StringHelper.build(hour, ":", minute);
		}    	
    }
    
    /**
     * Maps the given world time (0 - 24000) to a time label e.g. morning. 
     * 
     * @param   time   the world time (0 - 24000)
     * @return         The time label as string.
     */
    @NotNull
    public static String getTimeLabel(int time)
    {
    	if (time <= 1000) // 6 - 7
    	{
    		return "Sonnenaufgang";
    	}
    	else if (time <= 6000) // 7 - 12
    	{
    		return "Vormittag";
    	}
    	else if (time <= 7000) // 12 - 13
    	{
    		return "Mittag";
    	}
    	else if (time <= 11000) // 13 - 17
    	{
    		return "Nachmittag";
    	}
    	else if (time <= 13000) // 17 - 19 
    	{
    		return "Abend";
    	}
    	else if (time <= 14000) // 19 - 20
    	{
    		return "Sonnenuntergang";
    	}
    	else if (time <= 24000) // 20 - 6
    	{
    		return "Nacht";
    	}
    	return "";
    }
    
    /**
     * Sends the given message delayed to a player.
     * 
     * @param   plugin    the java plugin instance to create a delayed task
     * @param   player    the target player
     * @param   message   the message to send
     * @param   ticks     the amount of ticks the message will be delayed
     */
    public static void sendMessageDelayed(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull String message, long ticks)
    {
    	new BukkitRunnable()
		{				
			@Override
			public void run()
			{
				player.sendMessage(message);
			}
		}.runTaskLater(plugin, ticks);
    }
    
    /**
     * Sends the given messages delayed to a player.
     * 
     * @param   plugin     the java plugin instance to create a delayed task
     * @param   player     the target player
     * @param   messages   the messages to send
     * @param   ticks      the amount of ticks the messages will be delayed
     */
    public static void sendMessagesDelayed(@NotNull JavaPlugin plugin, @NotNull Player player,  @NotNull List<String> messages, long ticks)
    {
    	long tickCounter = ticks;
    	
    	for (String message : messages)
    	{
    		sendMessageDelayed(plugin, player, message, tickCounter);
    		tickCounter += ticks;
    	}
    }
}
