package huff.lib.various.structures.configuration;

import org.jetbrains.annotations.NotNull;

/**
 * A interface for a key value storage that needs a guaranteed default value e.g. the hufferlinge configuration.
 * 
 * @param   <T>   the type of the value that is stored
 */
public interface KeyDefaultValue<T>
{
	/**
	 * Gets the key of a value e.g. the path for a configuration value.
	 * 
	 * @return   The key.
	 */
	@NotNull
	public abstract String getKey();
	
	/**
	 * Gets a guaranteed default value to the key.
	 * 
	 * @return  The default key.
	 */
	@NotNull
	public abstract T getDefaultValue();
}
