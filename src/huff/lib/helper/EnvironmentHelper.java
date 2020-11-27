package huff.lib.helper;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.interfaces.Action;

public class EnvironmentHelper
{
	private EnvironmentHelper() { }
	
	public static void initDayligthCircle(@NotNull JavaPlugin plugin, @Nullable World world, @Nullable Action action)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		
		if (world == null)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot init daylight circle in a null world.");
			return;
		}
		world.setTime(0);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run()
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
			}
		}, 20, 5);
	}
}
