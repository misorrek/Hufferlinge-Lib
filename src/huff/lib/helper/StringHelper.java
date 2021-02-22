package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * A helper class containing static string methods.
 */
public class StringHelper
{
	private StringHelper() { }
	
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
	@NotNull
	public static String build(@NotNull Object... objects)
	{
		Validate.notNull(objects, "The build-part-objects cannot be null.");
		
		final StringBuilder builder = new StringBuilder();
		
		for (Object object : objects)
		{
			builder.append(object);
		}
		return builder.toString();
	}
	
	@NotNull
	public static String toValueList(Class<? extends Enum<?>> enumClass)
	{
		final StringBuilder builder = new StringBuilder();
		final Enum<?>[] enumValues = enumClass.getEnumConstants();
		
		for (int i = 0; i < enumValues.length; i++)
		{
			if (i != (enumValues.length - 1))
			{
				builder.append(enumValues[i].toString() + ", ");
			}
			else
			{
				builder.append(enumValues[i].toString());
			}
		}	
		return builder.toString();
	}
}
