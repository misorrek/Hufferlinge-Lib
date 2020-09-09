package huff.lib.manager;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.MessageHelper;
import redis.clients.jedis.Jedis;

public class RedisManager
{
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
	
	public void connect()
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
}
