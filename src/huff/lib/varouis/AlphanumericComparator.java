package huff.lib.varouis;

import java.util.Comparator;

public class AlphanumericComparator implements Comparator<String>
{
	@Override
	public int compare(String stringCurrent, String stringNext)
	{
		return stringCurrent.compareTo(stringNext);
	}
}
