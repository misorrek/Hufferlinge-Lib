package huff.lib.helper;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.interfaces.Action;

/**
 * A helper class containing static game environment methods.
 * Covers for example world functionality.
 */
public class EnvironmentHelper
{
	private EnvironmentHelper() { }
	
	/**
	 * Initializing a custom daylight cycle in a given world.
	 * Also possible to give a action which get called every iteration.  
	 * 
	 * @param   plugin   the java plugin instance
	 * @param   world    the target world
	 * @param   action   a optional action to run custom functionality
	 */
	public static void initDaylightCycle(@NotNull JavaPlugin plugin, @Nullable World world, @Nullable Action action)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		
		if (world == null)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot initialize daylight circle in a null world.");
			return;
		}
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setTime(0);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
		{
			final long worldTime = world.getTime();
			
			if (worldTime < 24000) 
			{
				world.setTime(worldTime + 10);
			}
			else
			{
				world.setTime(0);
			}				
			
			if (action != null)
			{
				action.execute(world);
			}	
		}, 20, 5);
	}
}
