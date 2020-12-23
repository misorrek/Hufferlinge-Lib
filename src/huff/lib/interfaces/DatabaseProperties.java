package huff.lib.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * A data interface that specifies all important details for a database connection.
 */
public interface DatabaseProperties
{
	@NotNull public abstract String getHost();
	@NotNull public abstract String getPort();
	@NotNull public abstract String getDatabasename();
	@NotNull public abstract String getUsername();
	@NotNull public abstract String getPassword();
}
