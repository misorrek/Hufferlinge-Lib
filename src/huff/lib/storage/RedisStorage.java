package huff.lib.storage;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.RedisManager;
import redis.clients.jedis.Jedis;

/**
 * Represents a abstract redis database storage for a specific key pattern.
 * Contains general key and value handling like creating and parsing keys or get the entry count.
 */
public abstract class RedisStorage 
{
	public RedisStorage(@NotNull RedisManager redisManager, boolean withAutoKey)
	{
		Validate.notNull((Object) redisManager, "The redis-manager cannot be null.");	
		
		this.redisManager = redisManager;
		this.withAutoKey = withAutoKey;
	}
	
	protected final RedisManager redisManager;
	
	private final boolean withAutoKey;
	
	/**
	 * Gets the pattern for a redis storage to connect with a key to create a unique one.
	 * 
	 * @return   The pattern.
	 */
	@NotNull
	protected abstract String getKeyPattern();
	
	/**
	 * Gets the current count of entries that have the pattern of this storage.
	 * 
	 * @return   The entry count.
	 */
	public int getEntryCount()
	{
		return getCombinedKeys().size();
	}
	
	/**
	 * Gets all with the pattern of this storage combined keys.
	 * 
	 * @return   The combined keys.
	 */
	@NotNull
	protected Set<String> getCombinedKeys()
	{
		try (final Jedis jedis = redisManager.getJedis())
		{
			return jedis.keys('*' + getKeyPattern() + '*');
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return new HashSet<>();
	}
	
	/**
	 * Gets all with the pattern of this storage combined keys, but without the pattern.
	 * 
	 * @return   The pattern removed keys.
	 */
	@NotNull 
	protected Set<String> getKeys()
	{
		return getCombinedKeys().stream().map(this::getKey).collect(Collectors.toSet());
	}
	
	/**
	 * Combines a key with the pattern of this storage to create a unique combined key.
	 * 
	 * @param   key   the key to combine
	 * @return        The combined key.
	 */
	@NotNull
	protected String getCombinedKey(@NotNull String key)
	{
		if (withAutoKey)
		{
			return getAutoCombinedKey();
		}
		else
		{
			Validate.notNull((Object) key, "The key cannot be null");
			
			return getKeyPattern() + key;
		}
	}
	
	/**
	 * Combines a key with the pattern of this storage to create a unique combined key.
	 * The given object will be converted to string.
	 * 
	 * @param   key   the key to combine
	 * @return        The combined key.
	 */
	@NotNull
	protected String getCombinedKey(@NotNull Object key)
	{
		if (withAutoKey)
		{
			return getAutoCombinedKey();
		}
		else
		{
			Validate.notNull(key, "The key cannot be null");
			
			return getKeyPattern() + key.toString();
		}
	}
	
	/**
	 * Combines the next free id with the pattern of this storage to create a unique combined key.
	 * 
	 * @return        The auto combined key.
	 */
	@NotNull
	protected String getAutoCombinedKey()
	{
		Validate.isTrue(withAutoKey, "Cannot create auto combined key with auto key deactivated.");
		
		return getKeyPattern() + getEntryCount();
	}
	
	/**
	 * Retrieves the key from a combined key.
	 * 
	 * @param   key   the combined key
	 * @return        The key without the pattern.
	 */
	@NotNull
	protected String getKey(@NotNull String key)
	{
		Validate.notNull((Object) key, "The key cannot be null");
		
		return key.replace(getKeyPattern(), "");
	}
}
