package huff.lib.various.structures;

import org.jetbrains.annotations.Nullable;

public class StringPair
{
	public StringPair(@Nullable String value1, @Nullable String value2)
	{
		this.value1 = value1 != null ? value1 : "";
		this.value2 = value2 != null ? value2 : "";
	}
	
	public final String value1;
	public final String value2;
}
