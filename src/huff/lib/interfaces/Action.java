package huff.lib.interfaces;

import org.jetbrains.annotations.Nullable;

public interface Action
{
	public void execute(@Nullable Object... params);
}
