package huff.lib.helper;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.various.Action;

/**
 * A helper class containing static game environment methods.
 * Covers for example world functionality.
 */
public class EnvironmentHelper
{
	public static final int WORLDTIME_MIN = 0;
	public static final int WORLDTIME_MAX = 24000;
	
	private EnvironmentHelper() { }
	
	/**
	 * Initializing a custom daylight cycle in a given world.
	 * Also possible to give a action which get called every iteration.  
	 * 
	 * @param   plugin   the java plugin instance
	 * @param   world    the target world
	 * @param   action   a optional action to run custom functionality
	 */
	public static void initDaylightCycle(@NotNull JavaPlugin plugin, @Nullable World world, int stepSize, int tickPeriod, @Nullable Action action)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		
		if (world == null)
		{
			Bukkit.getLogger().log(Level.WARNING, "Cannot initialize daylight circle in a null world.");
			return;
		}
		final long currentTime = world.getTime();
		
		world.setTime(currentTime - (currentTime % stepSize)); 
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
		{
			final long worldTime = world.getTime();
			
			if (worldTime < WORLDTIME_MAX) 
			{
				world.setTime(worldTime + stepSize);
			}
			else
			{
				world.setTime(WORLDTIME_MIN);
			}				
			
			if (action != null)
			{
				action.execute(world);
			}	
		}, 0, tickPeriod);
	}
}
