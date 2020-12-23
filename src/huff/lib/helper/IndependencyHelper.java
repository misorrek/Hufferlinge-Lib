package huff.lib.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class containing static dependency handling methods.
 * Covers for the "net.minecraft.server" and the "craftbukkit" dependency.
 */
public class IndependencyHelper 
{
	private static final int TICK_SECOND = 20;
	
	private IndependencyHelper() { }
	
	// G E N E R A L
	
	/**
	 * Tries to get the specified value from the enum in the given dependency.
	 * Returns null if the enum cannot be found.
	 * 
	 * @param   dependencyKind   the dependency in which the class should be found
	 * @param   enumName         the name of the target enum
	 * @param   enumValue        the name of the target enum value
	 * @return                   An enum instance representing the specified value.
	 */
	public static <T extends Enum<T>> @Nullable T getEnumValueByName(DependencyKind dependencyKind, @NotNull String enumName, @NotNull String enumValue)
	{
		final Class<T> enumClass = getDependencyEnumClass(dependencyKind, enumName);
		
		return enumClass != null ? Enum.valueOf(enumClass, enumValue) : null;
	}
	
	/**
	 * Tries to create a instance of the specified class from the given dependency.
	 * Also tries to use the parameters for the constructor.
	 * Returns null if the class cannot be created.
	 * 
	 * @param   dependencyKind   the dependency in which the class should be found
	 * @param   className        the name of the target class
	 * @param   params           additional parameters for the constructor
	 * @return                   An instance of the specified class.
	 */
	public static @Nullable Object createInstance(DependencyKind dependencyKind, @NotNull String className, @Nullable Object... params)
	{
		final Class<?> dependencyClass = getDependencyClass(dependencyKind, className);
				
		if (dependencyClass != null)
		{
			return createInstance(dependencyClass, params);
		}
		return null;
	}
	
	/**
	 * Tries to create a instance of the given class.
	 * Also tries to use the parameters for the constructor.
	 * Returns null if the class cannot be created.
	 * 
	 * @param   dependencyClass   the target class
	 * @param   params            additional parameters for the constructor
	 * @return                    An instance of the given class.
	 */
	public static @Nullable Object createInstance(@Nullable Class<?> dependencyClass, @Nullable Object... params)
	{
		if (dependencyClass == null)
		{
			return null;
		}
		
		try
		{				
			List<Class<?>> paramClasses = new ArrayList<>();
			
			for (Object param : params)
			{
				paramClasses.add(param.getClass());
			}				
			return dependencyClass.getConstructor(paramClasses.toArray(new Class<?>[0])).newInstance(params);
		} 
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant create instance named \"%s\" with params \"%s\".", dependencyClass.getName(), Arrays.toString(params)), exception);
			return null;
		}
	}
	
	/**
	 * Tries to invoke the specified method contained by the given object.
	 * Also tries to use the parameters for the method.
	 * Returns null if the method cannot be invoked but also when the method returns void.
	 * 
	 * @param   object       the object which contains the method
	 * @param   methodName   the name of the target method
	 * @param   params       additional parameters for the method
	 * @return               The retun value of the invoked method.
	 */	
	public static @Nullable Object invokeMethod(@Nullable Object object, @NotNull String methodName, @Nullable Object... params)
	{
		if (object == null)
		{
			return null;
		}
		
		try
		{
			List<Class<?>> paramClasses = new ArrayList<>();
			
			for (Object param : params)
			{
				paramClasses.add(param.getClass());
			}
			return object.getClass().getMethod(methodName, paramClasses.toArray(new Class<?>[0])).invoke(object, params);
		} 
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant invoke method named \"%s\" from object \"%s\" with params \"%s\".", methodName, object, Arrays.toString(params)), exception);
			return null;
		}
	}
	
	/**
	 * Tries to invoke the specified static method contained in the class from the given dependency.
	 * Also tries to use the parameters for the method.
	 * Returns null if the method cannot be invoked but also when the method returns void.
	 * 
	 * @param   dependencyKind   the dependency in which the class should be found
	 * @param   className        the name of the target class
	 * @param   methodName       the name of the target method
	 * @param   params           additional parameters for the method
	 * @return                   The retun value of the invoked method.
	 */	
	public static @Nullable Object invokeStaticMethod(DependencyKind dependencyKind, @NotNull String className, @NotNull String methodName, @Nullable Object... params)
	{
		final Class<?> dependencyClass = getDependencyClass(dependencyKind, className);
			
		return invokeStaticMethod(dependencyClass, methodName, params);
	}
	
	/**
	 * Tries to invoke the specified static method contained in the given class.
	 * Also tries to use the parameters for the method.
	 * Returns null if the method cannot be invoked but also when the method returns void.
	 * 
	 * @param   dependencyClass   the target class
	 * @param   methodName        the name of the target method
	 * @param   params            additional parameters for the method
	 * @return                    The retun value of the invoked method.
	 */	
	public static @Nullable Object invokeStaticMethod(@Nullable Class<?> dependencyClass, @NotNull String methodName, @Nullable Object... params)
	{
		if (dependencyClass == null)
		{
			return null;
		}
		
		try
		{			
			List<Class<?>> paramClasses = new ArrayList<>();
			
			for (Object param : params)
			{
				paramClasses.add(param.getClass());
			}
			return dependencyClass.getMethod(methodName, paramClasses.toArray(new Class<?>[0])).invoke(null, params);
		} 
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant invoke static method named \"%s\" with params \"%s\".", methodName, Arrays.toString(params)), exception);
			return null;
		}
	}
	
	private static @Nullable Class<?> getDependencyClass(DependencyKind dependencyKind, @NotNull String className) 
	{
		Validate.notNull((Object) className, "The class-name cannot be null.");
		
		final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try 
		{
			return Class.forName(StringHelper.build(dependencyKind.getLabel(), '.', version, '.', className));
		} 
		catch (ClassNotFoundException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant get version-depent-class named \"%s\" in version \"%s\" from package \"%s\".", 
					                                           className, version, dependencyKind.getLabel()), exception);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static @Nullable <T extends Enum<T>> Class<T> getDependencyEnumClass(DependencyKind dependencyKind, @NotNull String enumName)
	{
		final Class<?> enumClass = getDependencyClass(dependencyKind, enumName);
		
		return enumClass != null && enumClass.isEnum() ? (Class<T>) enumClass : null;
	}
	
	// P A C K E T
	
	/**
	 * Sends the packet represented by the given object to the specified player.
	 * 
	 * @param   player   the target player
	 * @param   packet   the packet object
	 */	
	public static void sendPacket(@NotNull Player player, @Nullable Object packet) 
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		if (packet == null)
		{
			return;
		}
		
		try 
		{
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerCon = handle.getClass().getField("playerConnection").get(handle);
			playerCon.getClass().getMethod("sendPacket", getDependencyClass(DependencyKind.NMS, "Packet")).invoke(playerCon, packet);
		} 
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot send packet to player \"%s\".", player), exception);
		}
	}
	
	/**
	 * Sends the packet represented by the given object to the specified player with a certain delay.
	 * 
	 * @param   player   the target player
	 * @param   packet   the packet object
	 * @param   ticks    the amount of ticks to delay
	 */	
	public static void sendPacketDelayed(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull Object packet, long ticks) 
	{
		Validate.notNull((Object) plugin, "The plugin cannot be null.");
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull(packet, "The packet-object cannot be null.");
		
		new BukkitRunnable()
		{				
			@Override
			public void run()
			{
				sendPacket(player, packet);
			}
		}.runTaskLater(plugin, ticks);
	}
	
	/**
	 * Sends the packet represented by the given object to the specified player with a certain delay.
	 * The packet will send asynchronously, so other tasks won't be blocked.
	 *
	 * @param   player   the target player
	 * @param   packet   the packet object
	 * @param   ticks    the amount of ticks to delay
	 */	
	public static void sendPacketDelayedAsync(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull Object packet, int ticks) 
	{
		Validate.notNull((Object) plugin, "The plugin cannot be null.");
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull(packet, "The packet-object cannot be null.");
		
		new BukkitRunnable()
		{				
			@Override
			public void run()
			{
				sendPacket(player, packet);
			}
		}.runTaskLaterAsynchronously(plugin, ticks);
	}
	
	// U T I L
	
	/**
	 * Packs a message in to a NBT json text.
	 *
	 * @param   message   the message to pack
	 * @return            The packed message.
	 */
	public static @NotNull String getJsonText(@NotNull String message)
	{
		Validate.notNull((Object) message, "The message cannot be null.");
		
		return StringHelper.build("{\"text\": \"", message, "\"}");
	}
	
	/**
	 * Converts a location to a Vec3D object from the NMS dependency.
	 *
	 * @param   location   the location to convert
	 * @return             The Vec3D object.
	 */
	public static @NotNull Object getLocationToVec3D(@NotNull Location location)
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		
		final Object vec3DObject = createInstance(DependencyKind.NMS, "Vec3D", location.getX(), location.getY(), location.getZ());
		
		if (vec3DObject != null)
		{
			return vec3DObject;
		}
		else
		{
			throw new NullPointerException(String.format("Cannot create Vec3D object from location \"%s\"", location));
		}
	}
	
	// T I M E
	
	/**
	 * Gets the system ticks that represent the given amount of seconds.
	 *
	 * @param   seconds   the amount of seconds wanted as ticks
	 * @return            The tick amount.
	 */
	public static long getSecondsInTicks(int seconds)
	{
		return (long) TICK_SECOND * seconds;
	}
	
	/**
	 * Sends the given player a time packet to set the display time for the current shown title.
	 * The time packet includes:
	 * duration : the time in ticks when the title is fully visible
	 * infade : the time in ticks how long the title takes from zero to hundred percent visibility
	 * outfade : the time in ticks how long the title takes from hundred to zero percent visibility
	 *
	 * @param   player     the target player
	 * @param   duration   the amount of ticks the title is shown
	 * @param   infade     the amount of ticks the title is infading
	 * @param   outfade    the amount of ticks the title is outfading
	 */
	public static void sendTime(@NotNull Player player, int duration, int infade, int outfade)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final Object packet = createInstance(DependencyKind.NMS, "PacketPlayOutTitle", getEnumByName(DependencyKind.NMS, "PacketPlayOutTitle.EnumTitleAction", "TIMES"), null, infade, duration, outfade);
		
		sendPacket(player, packet);
	}
	
	// D I S P L A Y S
	
	private static void sendRawTitle(@NotNull Player player, @NotNull String title, @NotNull String subTitle)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) player, "The title cannot be null.");
		Validate.notNull((Object) player, "The subtitle cannot be null.");
		
		final Object serializedTitle = invokeStaticMethod(DependencyKind.NMS, "ChatSerializer", "a", getJsonMessage(title));
		final Object serializedSubTitle = invokeStaticMethod(DependencyKind.NMS, "ChatSerializer", "a", getJsonMessage(subTitle));
		final Object packet = createInstance(DependencyKind.NMS, "PacketPlayOutTitle", getEnumByName(DependencyKind.NMS, "PacketPlayOutTitle.EnumTitleAction", "TITLE"), serializedTitle);
		final Object packet2 = createInstance(DependencyKind.NMS, "PacketPlayOutTitle", getEnumByName(DependencyKind.NMS, "PacketPlayOutTitle.EnumTitleAction", "SUBTITLE"), serializedSubTitle);
	
		sendPacket(player, packet);
		sendPacket(player, packet2);
	}
	
	/**
	 * Sends the given player a title packet with the specified title and subtitle.
	 * The title is shown the certain amount of ticks set by the duration.
	 * The infade (time how long the title takes from zero to hundred percent visibility) and the
         * outfade (the opposite function) are set to twenty ticks or one second.
	 *
	 * @param   player     the target player
	 * @param   title      the title to be shown
	 * @param   subtitle   the subtitle to be shown
	 * @param   duration   the amount of ticks the title is shown
	 */
	public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int duration) 
	{
		sendTitle(player, title, subtitle, duration, TICK_SECOND, TICK_SECOND);
	}
	
	/**
	 * Sends the given player a title packet with the specified title and subtitle.
	 * The title is shown the certain amount of ticks set by the duration.
	 * The infade (time how long the title takes from zero to hundred percent visibility) and the
         * outfade (the opposite function) are set to given values.
	 *
	 * @param   player     the target player
	 * @param   title      the title to be shown
	 * @param   subtitle   the subtitle to be shown
	 * @param   duration   the amount of ticks the title is shown
	 * @param   infade     the amount of ticks the title is infading
	 * @param   outfade    the amount of ticks the title is ouffading
	 */
	public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int duration, int infade, int outfade) 
	{
		sendRawTitle(player, title, subtitle);
		sendTime(player, ticks, infade, outfade);
	}
	
	/**
	 * Sends the given player a info above the hotbar containing the specified message.
	 *
	 * @param   player    the target player
	 * @param   message   the message to be shown
	 */
	public static void sendGameInfo(@NotNull Player player, @NotNull String message)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) player, "The message cannot be null.");
		
		final Object serializedMessage = invokeStaticMethod(DependencyKind.NMS, "ChatSerializer", "a", getJsonMessage(message));
		final Object packet = createInstance(DependencyKind.NMS, "PacketPlayOutChat", serializedMessage, getEnumByName(DependencyKind.NMS, "ChatMessageType", "GAME_INFO"), player.getUniqueId());
		
		sendPacket(player, packet);
	}
	
	// I T E M

	/**
	 * Add a custom NBT tag to the given item stack.
	 * The NBT tag consists of an for the item unique key and the value.
	 *
	 * @param   itemStack   the target item stack
	 * @param   key         the key associated with the value
	 * @param   value       the value to be stored
	 * @return              The item stack with the added NBT tag.
	 */
	public static @NotNull ItemStack getTaggedItemStack(@NotNull ItemStack itemStack, @NotNull String key, @NotNull String value) 
	{
		final Class<?> craftItemStackClass = getDependencyClass(DependencyKind.CRAFTBUKKIT, "inventory.CraftItemStack");
		final Object nmsItemStack = invokeStaticMethod(craftItemStackClass, "asNMSCopy", itemStack);
		final Object nmsItemCompound = (boolean) invokeMethod(nmsItemStack, "hasTag") ? invokeMethod(nmsItemStack, "getTag") : createInstance(DependencyKind.NMS, "NBTTagCompound");
		
		invokeMethod(nmsItemCompound, "setString", key, value);
		invokeMethod(nmsItemStack, "setTag", nmsItemCompound);
	
		final Object taggedItemStack = invokeStaticMethod(craftItemStackClass, "asBukkitCopy", nmsItemStack);
		
		if (taggedItemStack != null)
		{
			return (ItemStack) taggedItemStack;
		}	
		else
		{
			throw new NullPointerException(String.format("Cannot add tag \"%s\" with key \"%s\" to itemstack \"%s\"", value, key, itemStack));
		}	
	}

	/**
	 * Gets a NBT tag from the given item stack by a for the item unique key.
	 * If nothing can be obtained null gets returned.
	 *
	 * @param   itemStack   the target item stack
	 * @param   key         the key associated with the wanted value
	 * @return              The obtained value associated with the key.
	 */
	public static @Nullable String getTagFromItemStack(@NotNull ItemStack itemStack, @NotNull String key)
	{
		final Class<?> craftItemStackClass = getDependencyClass(DependencyKind.CRAFTBUKKIT, "inventory.CraftItemStack");
		final Object nmsItemStack;
		
		if (itemStack.getClass().equals(craftItemStackClass))
		{		
			nmsItemStack = invokeStaticMethod(craftItemStackClass, "asNMSCopy", ItemStack.deserialize(itemStack.serialize()));
		}
		else
		{
			nmsItemStack = invokeStaticMethod(craftItemStackClass, "asNMSCopy", itemStack);
		}

		if (!(boolean) invokeMethod(nmsItemStack, "hasTag"))
		{	
			return null;		
		}	
		final Object nmsItemCompound = invokeMethod(nmsItemStack, "getTag");
		final String value = (String) invokeMethod(nmsItemCompound, "getString", key);

		return value != null ? value : null;
	}
}
