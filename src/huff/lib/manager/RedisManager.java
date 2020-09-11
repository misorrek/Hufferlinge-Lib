package huff.lib.manager;

import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.MessageHelper;
import huff.lib.interfaces.RedisProperties;
import redis.clients.jedis.Jedis;

public class RedisManager
{
	public RedisManager(@NotNull RedisProperties redisProperties)
	{
		this.host = redisProperties.getHost();
		this.port = redisProperties.getPort();
	}
	
	public RedisManager(@NotNull String host, int port)
	{	
		this.host = host;
		this.port = port;
	}
	
	private final String host;
	private final int port;
	
	private Jedis jedis;
	
	public boolean isConnected()
	{
		return jedis != null && jedis.isConnected();
	}
	
	public @Nullable Jedis getJedis()
	{
		return jedis;
	}
	
	public void connect() // WAS GEMEINT MIT NICHT DAUERHAFTEN CONNECTION?
	{
		if (!isConnected())
		{
			try 
			{
				if (jedis == null)
				{
					jedis = new Jedis(host, port);
				}
				else
				{
					jedis.connect();
				}
				MessageHelper.sendConsoleMessage("Redis-Ping : " + jedis.ping());
				MessageHelper.sendConsoleMessage("Redis-Verbindung erfolgreich hergestellt.");
			} 
			catch (Exception exception) 
			{
				Bukkit.getLogger().log(Level.SEVERE, "Redis-Verbindung fehlgeschlagen. Daten hinterlegt?", exception);
			}
		}
	}

	public void disconnect()
	{
		if (isConnected())
		{
			jedis.disconnect();
		}
	}
	
	public boolean existKey(@NotNull String key)
	{
		Validate.notNull((Object) key, "The key cannot be null.");
		
		if (!isConnected())
		{
			try 
			{
				return jedis.exists(key);
			} 
			catch (Exception exception) 
			{
				Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
			}
		}	
		return false;
	}
	
	public boolean addMap(@NotNull String key, Map<String, String> fieldValuePairs) 
	{
		if (!isConnected() || !existKey(key))
		{
			try 
			{
				jedis.hmset(key, fieldValuePairs);
				return true;
			} 
			catch (Exception exception) 
			{
				Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
			}
		}
		return false;
	}
	
	public @Nullable String getFieldValue(@NotNull String key, @NotNull String field)
	{
		if (!isConnected() || existKey(key))
		{
			try
			{
				return jedis.hget(key, field);
			}
			catch (Exception exception) 
			{
				Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
			}
		}
		return null;
	}
	
	public @Nullable Map<String, String> getAlllValues(@NotNull String key)
	{
		if (!isConnected() || existKey(key))
		{
			try
			{
				return jedis.hgetAll(key);
			}
			catch (Exception exception) 
			{
				Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
			}
		}
		return null;
	}
	
	public void updateFieldValue(@NotNull String key, @NotNull String field, @NotNull String value)
	{
		if (!isConnected() || existKey(key))
		{
			try
			{
				jedis.hset(key, field, value);
			}
			catch (Exception exception) 
			{
				Bukkit.getLogger().log(Level.SEVERE	, "Redis-Statement cannot be executed.", exception);
			}
		}
	}
}
