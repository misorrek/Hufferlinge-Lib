package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class containing static entity handling methods.
 * Covers entity identification and tagging.
 */
public class EntityHelper
{
	public static final String ENTITYKEY_FOLLOWLOOK = "follow_look"; //TODO Save default look
	
	private EntityHelper() { }
	
	// T A G G I N G
	
	/**
	 * Checks if an entity has a tag with the given key in the persistent data container.
	 *
	 * @param   entity   the entity the  persistent data container is from
	 * @param	plugin   the java plugin instance
	 * @param   key      the key to check
	 * @return           The check result.
	 */	
	public static boolean hasTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		
		return entity.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.STRING);
	}
	
	/**
	 * Gets a tag with the given key in the persistent data container from the specified entity.
	 *
	 * @param   entity   the entity the  persistent data container is from
	 * @param	plugin   the java plugin instance
	 * @param   key      the key to get the value from
	 * @return           The tag value if existing.
	 */		
	@Nullable
	public static String getTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		
		return entity.getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
	}
	
	/**
	 * Sets a tag to the given key in the persistent data container from the specified entity.
	 *
	 * @param   entity   the entity the  persistent data container is from
	 * @param	plugin   the java plugin instance
	 * @param   key      the key to set
	 */	
	public static void setTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		
		setTag(entity, plugin, key, "");
	}
	
	/**
	 * Sets a tag with value to the given key in the persistent data container from the specified entity.
	 *
	 * @param   entity   the entity the  persistent data container is from
	 * @param	plugin   the java plugin instance
	 * @param   key      the key to set
	 * @param   value    the value to store
	 */	
	public static void setTag(@NotNull Entity entity, @NotNull JavaPlugin plugin, @NotNull String key, @NotNull String value)
	{
		Validate.notNull((Object) entity, "The entity cannot be null.");
		Validate.notNull((Object) plugin, "The java plugin instance cannot be null.");
		Validate.notNull((Object) key, "The key cannot be null.");
		Validate.notNull((Object) value, "The value cannot be null.");
		
		Bukkit.getConsoleSender().sendMessage("STORE :" + value);
		
		entity.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
	}
	
	// H O V E R T E X T
	
	public static Entity createHovertextEntity(@NotNull Location location, @NotNull String content, int line)
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		
		final double linePosition = 0.5 - (0.25 * (line - 1)); // 0,5 (First line position) - 0.25 (Height per line)
		final ArmorStand hovertextEntity = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, linePosition, 0), EntityType.ARMOR_STAND);

		hovertextEntity.setSmall(true);
		hovertextEntity.setVisible(false);
		hovertextEntity.setGravity(false);
		hovertextEntity.setInvulnerable(true);
		hovertextEntity.setCollidable(false);
		hovertextEntity.setCustomNameVisible(true);
		hovertextEntity.setCustomName(content);
		
		return hovertextEntity;
	}
}
