package huff.lib.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * A data interface that specifies all important details for a redis connection.
 */
public interface RedisProperties
{
	public abstract @NotNull String getHost();
	public abstract int getPort();
}
