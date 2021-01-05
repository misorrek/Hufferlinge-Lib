package huff.lib.helper;

import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import huff.lib.manager.mojangapi.MojangApiManager;

public class UserHelper
{
	private UserHelper() { }
	
	/**
	 * Tries to retrieve the uuid linked to the given username via mojang api manager.
	 * If nothing can retrieved or converted into an uuid null will be returned.
	 * 
	 * @param   username   the username
	 * @return             The uuid linked to the username.
	 */
	public static @Nullable UUID getUniqueId(@Nullable String username)
	{
		if (StringUtils.isEmpty(username))
		{	
			return null;
		}
		final Player player = Bukkit.getPlayer(username);
		
		if(player != null) 
		{
			MojangApiManager.updateCache(player.getUniqueId(), player.getName());
			return player.getUniqueId();
		}
		
		return MojangApiManager.getUUID(username);
	}
	
	/**
	 * Tries to retrieve the name linked to the given uuid string via mojang api manager.
	 * If nothing can retrieved null will be returned.
	 * 
	 * @param   uuid   the uuid as string
	 * @return         The username linked to the uuid.
	 */
	public static @Nullable String getUsername(@Nullable String uuid)
	{
		if (StringUtils.isEmpty(uuid))
		{	
			return null;
		}

		try 
		{
			return getUsername(UUID.fromString(uuid));
		}
		catch (IllegalArgumentException exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot get username from wrong formatted uuid.", exception);
			return null;
		}
	}
	
	/**
	 * Tries to retrieve the name linked to the given uuid via mojang api manager.
	 * If nothing can retrieved null will be returned.
	 * 
	 * @param   uuid   the uuid
	 * @return         The username linked to the uuid.
	 */
	public static @Nullable String getUsername(@Nullable UUID uuid)
	{
		if (uuid == null)
		{	
			return null;
		}
		final Player player = Bukkit.getPlayer(uuid);
		
		if(player != null)
		{
			MojangApiManager.updateCache(uuid, player.getName());
			return player.getName();
		}
		return MojangApiManager.getUsername(uuid);
	}
}
