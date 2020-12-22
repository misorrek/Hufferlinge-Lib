package huff.lib.menuholder;

public enum MenuExitType
{
	CLOSE,
	BACK,
	ABORT,
	NONE;
	
	public boolean isCloseType()
	{
		return this == CLOSE;
	}
	
	public boolean isReturnType()
	{
		return this == BACK || this == ABORT;
	}
}
