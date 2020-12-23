package huff.lib.interfaces;

import org.jetbrains.annotations.Nullable;

/**
 * A util interface to pass a function with parameters that can be executed by the receiver.
 */
public interface Action
{
	public void execute(@Nullable Object... params);
}
