package huff.lib.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.manager.RedisManager;

/**
 * A redis storage class that stores all user data.
 */
public class User extends RedisStorage 
{
	private static final String PATTERN = "user:";	
	
	public User(@NotNull RedisManager redisManager) 
	{
		super(redisManager, false);
	}

	@Override
	protected @NotNull String getKeyPattern() 
	{
		return PATTERN;
	}
	
	// U S E R
	
	public boolean existUser(@NotNull UUID uuid)
	{
		Validate.notNull((Object) uuid, "The uuid cannot be null");
		
		return redisManager.existKey(getCombinedKey(uuid.toString()));
	}
	
	@NotNull
	public List<UUID> getUsers()
	{
		return getUsers(null);
	}
	
	@NotNull
	public List<UUID> getUsers(@Nullable UUID filteredUuid)
	{
		final List<UUID> users = new ArrayList<>();
		final Set<String> keys = super.getKeys();
		
		for (String key : keys)
		{
			final UUID currentUuid = UUID.fromString(key); 
			
			if (filteredUuid != null && currentUuid.equals(filteredUuid))
			{
				continue;
			}
			users.add(currentUuid);
		}
		return users;
	}
}
