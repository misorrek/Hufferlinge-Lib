package huff.lib.menuholder;

/**
 * A enum that represents the wanted exit behavior for a menu inventory.
 */
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
