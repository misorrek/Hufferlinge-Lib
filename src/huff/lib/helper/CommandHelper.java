package huff.lib.helper;

import java.lang.reflect.Method;
import java.util.Arrays;
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
		
		//Map<String, Command> internalCommandMap = getInternalCommandMap();
		
		command.setAliases(Arrays.asList(aliases));
		getInternalCommandMap();
	}
	
	private static void getInternalCommandMap() 
	{
		try
		{
			Method getCommandMap = Bukkit.getServer().getClass().getMethod("getCommandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) getCommandMap.invoke(Bukkit.getServer());
				
			if (commandMap == null)
			{
				return;
			}
			commandMap.registerServerAliases();
			/*Field knownCommands = commandMap.getClass().getDeclaredField("knownCommands");
			knownCommands.setAccessible(true);
			
			return (HashMap<String, Command>) knownCommands.get(commandMap);*/
		}
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cant get internal command map.", exception);
		}
	}
}
