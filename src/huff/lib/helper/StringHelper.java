package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringHelper
{
	private StringHelper() { }
	
	public static boolean isNullOrEmpty(@Nullable String string)
	{
		return string == null || string.isEmpty();
	}
	
	public static boolean isNullOrWhitespace(@Nullable String string)
	{
		return string == null || string.trim().isEmpty();
	}
	
	public static boolean isNotNullOrEmpty(@Nullable String string)
	{
		return string != null && !string.isEmpty();
	}
	
	public static boolean isNotNullOrWhitespace(@Nullable String string)
	{
		return string != null && !string.trim().isEmpty();
	}
	
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
