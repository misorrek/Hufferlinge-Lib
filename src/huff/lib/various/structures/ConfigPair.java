package huff.lib.various.structures;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.ConfigManager;

public class ConfigPair<T> implements KeyDefaultValuePair<T>
{
	public ConfigPair(@NotNull String key, @NotNull T defaultValue, @NotNull Class<T> valueClass)
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
		Object configValue;
		
		if (valueClass == String.class)
		{
			configValue = ConfigManager.CONFIG.getString(key, (String) defaultValue);
		}
		else
		{
			configValue = ConfigManager.CONFIG.get(key);
		}
		
		if (configValue != null)
		{
			try
			{
				return valueClass.cast(configValue);
			} 
			catch (ClassCastException exception)
			{
				Bukkit.getLogger().log(Level.WARNING, exception, () -> "Cannot cast config value to " + valueClass.getName() + ".");
			}
		}		
		return defaultValue;
	}
}
