package huff.lib.listener;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();
		final Point point = LibConfig.ENTITY_FOLLOWLOOKRADIUS.getValue();
		
		for (Entity curEntity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), point.getX(), point.getY(), point.getZ()))
		{
			if (EntityHelper.hasTag(curEntity, plugin, EntityHelper.ENTITYKEY_FOLLOWLOOK))
			{
				curEntity.teleport(curEntity.getLocation().setDirection(player.getLocation().subtract(curEntity.getLocation()).toVector()));
			}
		}
	}
}
