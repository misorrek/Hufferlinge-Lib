package huff.lib.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandHelper
{
	private CommandHelper() { }
	
	public static void addAliases(@NotNull Command command, @Nullable String... aliases)
	{
		Validate.notNull((Object) command, "The command cannot be null.");
		
		Map<String, Command> internalCommandMap = getInternalCommandMap();
		
		if (internalCommandMap != null) 
		{
			for (String alias : aliases)
			{
				internalCommandMap.put(alias.toLowerCase(), command);
			}
		}
	}
	
	private static @Nullable HashMap<String, Command> getInternalCommandMap() 
	{
		try
		{
			Method getCommandMap = Bukkit.getServer().getClass().getMethod("getCommandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) getCommandMap.invoke(Bukkit.getServer());
			
			if (commandMap == null)
			{
				return null;
			}
			Field knownCommands = commandMap.getClass().getDeclaredField("knownCommands");
			knownCommands.setAccessible(true);
			
			return (HashMap<String, Command>) knownCommands.get(commandMap);
		}
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cant get internal command map.", exception);
			return null;
		}
	}
}
