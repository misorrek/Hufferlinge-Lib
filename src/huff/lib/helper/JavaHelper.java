package huff.lib.helper;

import org.jetbrains.annotations.NotNull;

/**
 * A helper class containing additional static java methods and constants.
 */
public class JavaHelper
{
	public static final int SECOND_IN_MILLIS = 1000;
	
	private JavaHelper() { }
	
	/**
	 * Checks if the specified object is null and in case it is returns the given default value.
	 * 
	 * @param   <T>            the value type
	 * @param   value          the value to check
	 * @param   defaultValue   the default value in case of null
	 * @return                 The value if its not null otherwise the default value.
	 */
	@NotNull
	public static <T> T getValueOrDefault(T value, T defaultValue) 
	{
	    return value == null ? defaultValue : value;
	}
	
	/**
	 * Gets the given seconds in milliseconds.
	 * 
	 * @param   seconds   the seconds wanted in milliseconds    
	 * @return            The milliseconds.
	 */
	public static int getSecondsInMillis(int seconds)
	{
		return seconds * SECOND_IN_MILLIS;
	}
}
