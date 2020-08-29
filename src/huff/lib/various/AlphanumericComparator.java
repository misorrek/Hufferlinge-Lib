package huff.lib.various;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public class AlphanumericComparator implements Comparator<String>
{
	@Override
	public int compare(@NotNull String stringCurrent, @NotNull String stringNext)
	{
		Validate.notNull((Object) stringCurrent, "The current string for comparison cannot be null.");
		Validate.notNull((Object) stringNext, "The next string for comparison cannot be null.");
		
		return stringCurrent.compareTo(stringNext);
	}
}
