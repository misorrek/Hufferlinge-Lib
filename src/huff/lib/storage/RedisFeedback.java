package huff.lib.storage;

/**
 * A enum that represents the result of an redis database action.
 */
public enum RedisFeedback 
{
	SUCCESS(0),
	NOENTRY(-1),
	NOCORRECTVALUE(-2),
	DUPLICATE(-3),
	UNKNOWN(-4);

	RedisFeedback(int code) 
	{
		this.code = code;
	}
	
	private final int code;
	
	public static RedisFeedback fromValue(double value)
	{
		if (value >= 0)
		{
			return SUCCESS;
		}	 
		final RedisFeedback[] codes = RedisFeedback.values();
		final int codeValue = (int) value;
		
		for (RedisFeedback code : codes)
		{
			if (codeValue == code.getCode())
			{
				return code;
			}
		}
		return UNKNOWN;
	}
	
	public int getCode()
	{
		return code;
	}
	
	public boolean isSuccess()
	{
		return this == SUCCESS;
	}
}
