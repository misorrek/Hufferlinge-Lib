package huff.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import huff.lib.manager.mojangapi.MojangApiManager;

/**
 * A listener class that handles player name cache at player join.
 */
public class CacheListener implements Listener
{	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		MojangApiManager.updateCache(event.getPlayer().getUniqueId(), event.getPlayer().getName());	
	}
}
