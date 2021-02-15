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

public class RedisManager
{	
	public RedisManager(@NotNull String host, int port)
	{	
		Validate.notNull((Object) host, "The redis host cannot be null.");
		
		this.jedisPool = new JedisPool(buildPoolConfig(), host, port);
	}

	public RedisManager()
	{
		this(LibConfig.REDIS_HOST.getValue(), 
			 LibConfig.REDIS_PORT.getValue());
	}
	
	private final JedisPool jedisPool;
	
	public @NotNull Jedis getJedis()
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
	
	public boolean deleteKey(@NotNull String key)
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.del(key);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return false;
	}
	
	public boolean addMap(@NotNull String key, Map<String, String> fieldValuePairs) 
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.hmset(key, fieldValuePairs);
			return true;
		} 
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return false;
	}
	
	public @Nullable String getFieldValue(@NotNull String key, @NotNull String field)
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
	
	public @Nullable Map<String, String> getAllValues(@NotNull String key)
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
	
	public boolean setFieldValue(@NotNull String key, @NotNull String field, @NotNull String value)
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.hset(key, field, value);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return false;
	}
	
	public boolean setValue(@NotNull String key, @NotNull String value)
	{
		try (final Jedis jedis = getJedis()) 
		{
			jedis.set(key, value);
		}
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
		}
		return false;
	}
}
