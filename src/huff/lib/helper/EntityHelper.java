package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EntityHelper
{
	public static final String ENTITYKEY_FOLLOWLOOK = "follow_look";
	
	private EntityHelper() { }
	
	public static boolean hasTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		
		return entity.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.STRING);
	}
	
	public static String getTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		
		return entity.getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
	}
	
	public static void setTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		
		setTag(entity, plugin, key, "");
	}
	
	public static void setTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key, @NotNull String value)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		Validate.notNull((Object) value, "The value cannot be null.");
		
		entity.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
	}
}
