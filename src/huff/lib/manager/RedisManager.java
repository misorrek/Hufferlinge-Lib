package huff.lib.manager;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.various.LibConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * A manager class that creates and holds a redis database connection in a jedis pool.
 */
public class RedisManager
{	
	public RedisManager(@NotNull String host, int port)
	{	
		Validate.notNull((Object) host, "The redis host cannot be null.");
		
		this.jedisPool = new JedisPool(buildPoolConfig(), host, port);
	}

	/**
	 * Uses the connection details from the configuration.
	 */
	public RedisManager()
	{
		this(LibConfig.REDIS_HOST.getValue(), 
			 LibConfig.REDIS_PORT.getValue());
	}
	
	private final JedisPool jedisPool;
	
	/**
	 * Gets a jedis connection from the current jedis pool.
	 * 
	 * @return   The jedis connection.
	 */
	@NotNull
	public Jedis getJedis()
	{
		return jedisPool.getResource();
	}

	private JedisPoolConfig buildPoolConfig() 
	{
	    final JedisPoolConfig poolConfig = new JedisPoolConfig();
	    
	    poolConfig.setMaxTotal(8);
	    poolConfig.setMaxIdle(8);
	    poolConfig.setMinIdle(0);
	    poolConfig.setTestOnBorrow(false);
	    poolConfig.setTestOnReturn(false);
	    poolConfig.setTestWhileIdle(true);
	    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(360).toMillis());
	    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(360).toMillis());
	    poolConfig.setNumTestsPerEvictionRun(3);
	    poolConfig.setBlockWhenExhausted(true);
	    
	    return poolConfig;
	}
	
	/**
	 * Destroys the creates jedis pool.
	 */
	public void destroyPool() 
	{
		try 
		{
			jedisPool.destroy();
		} 
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, "Redis-Disconnection failed.", exception);
		}		
	}
	
	// U T I L
	
	/**
	 * Checks if the given key exists in the redis database.
	 * 
	 * @param   key   the key to check
	 * @return        The check result.
	 */
	public boolean existKey(@NotNull String key)
	{
		Validate.notNull((Object) key, "The key cannot be null.");

		try (final Jedis jedis = getJedis()) 
		{
			return jedis.exists(key);
		} 
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return false;
	}
	
	/**
	 * Deletes the given key in the redis database.
	 * If the key does not exists nothing will be deleted.
	 * 
	 * @param   key   the key to delete
	 */
	public void deleteKey(@NotNull String key)
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.del(key);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
	}
	
	/**
	 * Sets a field value list to the given key in the redis databse.
	 * 
	 * @param   key               the key to store the field value list in
	 * @param   fieldValuePairs   the field value list to store
	 * @return                    Determines whether the action were successful or not.
	 */
	public boolean addMap(@NotNull String key, Map<String, String> fieldValuePairs) 
	{
		try (final Jedis jedis = getJedis()) 
		{
			if (jedis.hmset(key, fieldValuePairs).equals("OK"))
			{
				return true;
			}
		} 
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return false;
	}
	
	/**
	 * Gets the value of the specified field in the field value list stored in the given key in the redis database.
	 * If nothing can be retrieved null will be returned.
	 * 
	 * @param   key     the key that stores the field value list
	 * @param   field   the field in the field value list
	 * @return          The retrieved value.
	 */
	@Nullable
	public String getFieldValue(@NotNull String key, @NotNull String field)
	{
		try (final Jedis jedis = getJedis()) 
		{
			return jedis.hget(key, field);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return null;
	}
	
	/**
	 * Gets the entire field value list of the given key in the redis database.
	 * If nothing can be retrieved null will be returned.
	 * 
	 * @param   key   the key that stores the field value list
	 * @return        The retrieved field value list.
	 */
	@Nullable
	public Map<String, String> getAllValues(@NotNull String key)
	{
		try (final Jedis jedis = getJedis()) 
		{
			return jedis.hgetAll(key);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return null;
	}
	
	/**
	 * Sets the given value to the specified field in the field value list of the given key in the redis database.
	 * 
	 * @param   key     the key that stores the field value list
	 * @param   field   the field to store the value in
	 * @param   value   the value to store
	 */
	public void setFieldValue(@NotNull String key, @NotNull String field, @NotNull String value)
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.hset(key, field, value);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
	}
	
	/**
	 * Sets the given value to the given key in the redis database.
	 *
	 * @param   key     the key to store the value in
	 * @param   value   the value to store
	 */
	public void setValue(@NotNull String key, @NotNull String value)
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.set(key, value);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
	}
}
