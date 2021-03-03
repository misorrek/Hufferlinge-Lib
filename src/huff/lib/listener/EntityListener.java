package huff.lib.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.DataHelper;
import huff.lib.helper.EntityHelper;
import huff.lib.various.LibConfig;
import huff.lib.various.structures.Point;

/**
 * A listener class that handles follow look tagged entities at player move. 
 */
public class EntityListener implements Listener
{	
	public EntityListener(@NotNull JavaPlugin plugin)
	{
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		
		this.plugin = plugin;
	}
	
	private final JavaPlugin plugin;
	private final Map<Entity, Boolean> currentFollowLooking = new HashMap<>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();
		
		if (player.getGameMode() == GameMode.SPECTATOR || player.isInvisible())
		{
			return;
		}
		final Point point = LibConfig.ENTITY_FOLLOWLOOKRADIUS.getValue();
		
		for (Entity currentEntity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), point.getX(), point.getY(), point.getZ()))
		{
			if (EntityHelper.hasTag(currentEntity, plugin, EntityHelper.ENTITYKEY_FOLLOWLOOK))
			{
				currentEntity.teleport(currentEntity.getLocation().setDirection(player.getLocation().subtract(currentEntity.getLocation()).toVector()));
				
				currentFollowLooking.put(currentEntity, true);
			}
		}	
		final Iterator<Entry<Entity, Boolean>> iterator = currentFollowLooking.entrySet().iterator();
		
		while (iterator.hasNext()) 
		{
			Entry<Entity, Boolean> entry = iterator.next();
		    
			if (Boolean.TRUE.equals(entry.getValue()))
			{
				entry.setValue(false);
			}
			else
			{
				final Location defaultLookLocation = DataHelper.convertStringtoLocation(EntityHelper.getTag(entry.getKey(), plugin, EntityHelper.ENTITYKEY_FOLLOWLOOK));
				
				Bukkit.getConsoleSender().sendMessage("LOC :" + defaultLookLocation.toString());
				
				if (defaultLookLocation != null) 
				{
					entry.getKey().teleport(defaultLookLocation);
				}
				iterator.remove();
			}
		}
	}
}
