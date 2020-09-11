package huff.lib.interfaces;

import org.jetbrains.annotations.NotNull;

public interface RedisProperties
{
    public abstract @NotNull String getHost();
	public abstract int getPort();
}
