package huff.lib.helper;

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
	
	public static boolean isIn(boolean ignoreCase, String value, String... list)
	{
		for (String compareValue : list)
		{
			if (ignoreCase ? value.equalsIgnoreCase(compareValue) : value.equals(compareValue)) return true;
		}
		return false;
	}
}
