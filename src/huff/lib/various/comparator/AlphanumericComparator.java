package huff.lib.various.comparator;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * A comparer class to compare the alphanumeric value of two strings.
 */
public class AlphanumericComparator implements Comparator<String>
{
	@Override
	public int compare(@NotNull String currentString, @NotNull String nextString)
	{
		Validate.notNull((Object) currentString, "The current string for comparison cannot be null.");
		Validate.notNull((Object) nextString, "The next string for comparison cannot be null.");
		
		return currentString.compareTo(nextString);
	}
}
