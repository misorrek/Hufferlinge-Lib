package huff.lib.helper;

import org.jetbrains.annotations.NotNull;

public enum VersionDependency
{
	NMS("net.minecraft.server"),
	CRAFTBUKKIT("org.bukkit.craftbukkit");
	
	private VersionDependency(String label)
	{
		this.label = label;
	}
	
	private final String label;
	
	public @NotNull String getLabel()
	{
		return this.label;
	}
}
