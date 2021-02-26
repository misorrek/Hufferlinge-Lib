package huff.lib.helper;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	
	/**
	 * Tries to parse a double value from the given string.
	 * If the String is null or cannot be parsed -1 will be returned.
	 * 
	 * @param   doubleString   a string containing a double value
	 * @return                 The parsed double value.
	 */
	public static double tryParseDouble(@Nullable String doubleString)
	{		
		if (doubleString == null)
		{
			return -1;
		}	
		
		try
		{
			return Double.parseDouble(doubleString);
		}
		catch (NumberFormatException exception)
		{
			Bukkit.getLogger().log(Level.WARNING, "The double-value is invalid.", exception);
		}
		return -1;
	}
}
