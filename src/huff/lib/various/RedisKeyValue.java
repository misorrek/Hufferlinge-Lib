package huff.lib.various;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.RedisManager;
import redis.clients.jedis.Jedis;

public abstract class RedisKeyValue 
{
	public RedisKeyValue(@NotNull RedisManager redisManager)
	{
		Validate.notNull((Object) redisManager, "The redis-manager cannot be null.");	
		
		this.redisManager = redisManager;
	}
	
	protected final RedisManager redisManager;
	
	public abstract Set<String> getKeys();
	
	public abstract String getCombinedKey(@NotNull String key);
	
	@NotNull
	protected Set<String> getKeys(@NotNull String pattern)
	{
		Validate.notNull((Object) pattern, "The pattern cannot be null");
		
		try (final Jedis jedis = redisManager.getJedis())
		{
			return jedis.keys('*' + pattern + '*');
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return new HashSet<>();
	}
	
	@NotNull
	protected String getCombinedKey(@NotNull String pattern, @NotNull String key)
	{
		Validate.notNull((Object) pattern, "The pattern cannot be null");
		Validate.notNull((Object) key, "The key cannot be null");
		
		return pattern + key;
	}
}
