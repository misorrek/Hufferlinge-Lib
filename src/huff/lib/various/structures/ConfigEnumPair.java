package huff.lib.various.structures;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.ConfigManager;

public class ConfigEnumPair<T extends Enum<T>> implements KeyDefaultValuePair<T>
{
	public ConfigEnumPair(@NotNull String key, @NotNull T defaultValue, @NotNull Class<T> valueClass)
	{
		Validate.notNull((Object) key, "The config pair key cannot be null.");
		Validate.notNull(defaultValue, "The config pair default value cannot be null.");
		Validate.notNull(valueClass, "The config pair value class cannot be null.");
		
		this.key = key;
		this.defaultValue = defaultValue;
		this.valueClass = valueClass;
	}
	
	private final String key;
	private final T defaultValue;
	private final Class<T> valueClass;
	
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
	public T getDefaultValue()
	{
		return defaultValue;
	}
	
	@NotNull
	public T getValue()
	{
		final Object configValue = ConfigManager.CONFIG.get(key);
		
		if (configValue instanceof String)
		{		
			return Enum.valueOf(valueClass, (String) configValue);
		}		
		return defaultValue;
	}
}
