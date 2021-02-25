package huff.lib.various.structures.configuration;

import org.jetbrains.annotations.NotNull;

import huff.lib.various.structures.StringPair;

/**
 * A interface for a key value mapping storage especially for the hufferlinge configuration.
 * Can retrieve a value from the configuration itself or if nothing can be retrieved uses a default value.
 * 
 * @param   <T>   the type of the value that is stored
 */
public interface ConfigKeyValue<T> extends KeyDefaultValue<T>
{
	/**
	 * Gets the a configuration content link to this key.
	 * 
	 * @return   The content link.
	 */
	@NotNull
	public abstract String getKeyLink();
	
	/**
	 * Tries to retrieve a value from a configuration or returns a default value.
	 * 
	 * @return   The retrieved value.
	 */
	@NotNull
	public abstract T getValue();
	
	/**
	 * Tries to retrieve a value from a configuration or returns a default value.
	 * Also fill placeholder's with the given context parameters.
	 * 
	 * @return   The retrieved value.
	 */
	@NotNull
	public abstract T getValue(StringPair... contextParameters);
}
