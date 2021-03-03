package huff.lib.various.structures.configuration;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import huff.lib.manager.ConfigManager;
import huff.lib.various.structures.StringPair;

/**
 * A configuration key value store to retrieve a value or a default value from the config manager.
 * 
 * @param   <T>   the type of the stored value
 */
public class ConfigPair<T> implements ConfigKeyValue<T>
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
	public T getDefaultValue()
	{
		return defaultValue;
	}
	
	@Override
	public T getValue()
	{
		return getValue(new StringPair[0]);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T getValue(StringPair... contextParameters) 
	{
		Object configValue;
		
		if (valueClass == String.class)
		{
			configValue = ConfigManager.CONFIG.getString(key, (String) defaultValue, contextParameters);
		}
		else
		{
			configValue = ConfigManager.CONFIG.get(key);
		}
		
		if (configValue != null)
		{
			if (configValue instanceof String && valueClass.isEnum())
			{
				return (T) Enum.valueOf((Class) valueClass, (String) configValue);
			}
			
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
