package huff.lib.helper;

import org.jetbrains.annotations.NotNull;

/**
 * A enum that represents the origin package of a dependency.
 */
public enum DependencyKind
{
	NMS("net.minecraft.server"),
	CRAFTBUKKIT("org.bukkit.craftbukkit");
	
	private DependencyKind(String label)
	{
		this.label = label;
	}
	
	private final String label;
	
	public @NotNull String getLabel()
	{
		return this.label;
	}
}
