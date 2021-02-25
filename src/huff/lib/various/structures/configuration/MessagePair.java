package huff.lib.various.structures.configuration;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.MessageManager;
import huff.lib.various.structures.StringPair;

/**
 * A configuration key string value store to retrieve a value or a default value from the message manager.
 */
public class MessagePair implements ConfigKeyValue<String>
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
	
	@Override
	public String getKey()
	{
		return key;
	}
	
	@Override
	public String getKeyLink()
	{
		return "{" + key + "}";
	}
	
	@Override
	public String getDefaultValue()
	{
		return defaultMessage;
	}
	
	@Override
	public String getValue()
	{
		return getValue(new StringPair[0]);
	}
	
	@Override
	public String getValue(StringPair... contextParameters)
	{
		final String message = MessageManager.MESSAGE.getString(key, defaultMessage, contextParameters);
		
		if (message != null)
		{
			return message;
		}
		return defaultMessage;
	}
}
