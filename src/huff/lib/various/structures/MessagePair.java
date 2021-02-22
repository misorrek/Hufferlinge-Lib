package huff.lib.various.structures;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.MessageManager;

public class MessagePair implements KeyDefaultValuePair<String>
{
	public MessagePair(@NotNull String key, @NotNull String defaultMessage)
	{
		Validate.notNull((Object) key, "The message pair key cannot be null.");
		Validate.notNull((Object) defaultMessage, "The message pair default message cannot be null.");
		
		this.key = key;
		this.defaultMessage = defaultMessage;
	}
	
	private final String key;
	private final String defaultMessage;
	
	@NotNull
	public String getKey()
	{
		return key;
	}
	
	@NotNull
	public String getKeyLink()
	{
		return "{" + key + "}";
	}
	
	@NotNull
	public String getDefaultValue()
	{
		return defaultMessage;
	}
	
	@NotNull
	public String getMessage()
	{
		return getMessage(new StringPair[0]);
	}
	
	@NotNull
	public String getMessage(StringPair... contextParameters)
	{
		final String message = MessageManager.MESSAGE.getString(key, defaultMessage, contextParameters);
		
		if (message != null)
		{
			return message;
		}
		return defaultMessage;
	}
}
