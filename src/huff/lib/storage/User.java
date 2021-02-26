package huff.lib.storage;

import java.util.ArrayList;
import java.util.List;
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
		List<UUID> users = new ArrayList<>();
		
		getKeys().forEach(x -> users.add(UUID.fromString(x)));
		
		return users;
	}
}
