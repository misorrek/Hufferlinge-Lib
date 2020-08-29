package huff.lib.interfaces;

import org.jetbrains.annotations.NotNull;

public interface DatabaseProperties
{
	@NotNull public abstract String getHost();
	@NotNull public abstract String getPort();
	@NotNull public abstract String getDatabasename();
	@NotNull public abstract String getUsername();
	@NotNull public abstract String getPassword();
}
