package huff.lib.various;

public class Pair<V1, V2> 
{
	public Pair(V1 value1, V2 value2)
	{
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public final V1 value1;
	public final V2 value2;
}