package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class containing static string methods.
 */
public class StringHelper
{
	private StringHelper() { }
	
	/**
	 * Checks if a string is null or empty.
	 * 
	 * @param   value   the string to check
	 * @return          A boolean that gives the check result.
	 */
	public static boolean isNullOrEmpty(@Nullable String value)
	{
		return value == null || value.isEmpty();
	}

	/**
	 * Checks if a string is null, empty or has only whitespace.
	 * 
	 * @param   value   the string to check
	 * @return          A boolean that gives the check result
	 */
	public static boolean isNullOrWhitespace(@Nullable String value)
	{
		return value == null || value.trim().isEmpty();
	}
	
	/**
	 * Checks if a string is not null and empty.
	 * 
	 * @param   value   the string to check
	 * @return          A boolean that gives the check result
	 */
	public static boolean isNotNullOrEmpty(@Nullable String value)
	{
		return value != null && !value.isEmpty();
	}
	
	/**
	 * Checks if a string is not null, not empty or don't has only whitespaces.
	 * 
	 * @param   value   the string to check
	 * @return          A boolean that gives the check result
	 */
	public static boolean isNotNullOrWhitespace(@Nullable String value)
	{
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * Checks if the string is equals a string from the list.
	 * 
	 * @param   ignoreCase   option to (de-)activate case sensitivity
	 * @param   value   	 a string to compare
	 * @param   list	     a list of strings to compare
	 * @return           	 A boolean that gives the comparing result
	 */
	public static boolean isIn(boolean ignoreCase, @NotNull String value, @NotNull String... list)
	{
		Validate.notNull((Object) value, "The string-value cannot be null.");
		Validate.notNull((Object) list, "The string-list cannot be null.");
		
		for (String compareValue : list)
		{
			if (ignoreCase ? value.equalsIgnoreCase(compareValue) : value.equals(compareValue)) return true;
		}
		return false;
	}

	/**
	 * Checks if the string is contained in a string from the list.
	 * 
	 * @param   ignoreCase   option to (de-)activate case sensitivity
	 * @param   value   	 a string to compare
	 * @param   list	     a list of strings to compare
	 * @return           	 A boolean that gives the comparing result
	 */
	public static boolean contains(boolean ignoreCase, @NotNull String value, @NotNull String... list)
	{
		Validate.notNull((Object) value, "The string-value cannot be null.");
		Validate.notNull((Object) list, "The string-list cannot be null.");
		
		for (String compareValue : list)
		{
			if (ignoreCase ? value.toLowerCase().contains(compareValue.toLowerCase()) : value.contains(compareValue)) return true;
		}
		return false;
	}

	/**
	 * Builds all given objects with a StringBuilder to a string.
	 * 
	 * @param   objects   a list of object to build
	 * @return            The string built out of the objects
	 */
	public static @NotNull String build(@NotNull Object... objects)
	{
		Validate.notNull(objects, "The build-part-objects cannot be null.");
		
		final StringBuilder builder = new StringBuilder();
		
		for (Object object : objects)
		{
			builder.append(object);
		}
		return builder.toString();
	}
}
