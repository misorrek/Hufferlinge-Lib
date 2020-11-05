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

public class IndependencyHelper 
{
	private static final int TICK_SECOND = 20;
	
	private IndependencyHelper() { }
	
	// G E N E R A L
	
	private static @Nullable Class<?> getVersionDependentClass(VersionDependency versionDependency, @NotNull String className) 
	{
		Validate.notNull((Object) className, "The class-name cannot be null.");
		
		final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try 
		{
			return Class.forName(StringHelper.build(versionDependency.getLabel(), '.', version, '.', className));
		} 
		catch (ClassNotFoundException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant get version-depent-class named \"%s\" in version \"%s\" from package \"%s\".", 
					                                           className, version, versionDependency.getLabel()), exception);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static @Nullable <T extends Enum<T>> Class<T> getNMSEnum(VersionDependency versionDependency, @NotNull String enumName)
	{
		final Class<?> enumClass = getVersionDependentClass(versionDependency, enumName);
		
		return enumClass != null && enumClass.isEnum() ? (Class<T>) enumClass : null;
	}
	
	public static <T extends Enum<T>> T getEnumByName(VersionDependency versionDependency, @NotNull String enumName, @NotNull String enumValue)
	{
		final Class<T> enumClass = getNMSEnum(versionDependency, enumName);
		
		return enumClass != null ? Enum.valueOf(enumClass, enumValue) : null;
	}
	
	public static @Nullable Object createInstance(VersionDependency versionDependency, @NotNull String packetName, @Nullable Object... params)
	{
		try
		{
			final Class<?> packetClass = getVersionDependentClass(versionDependency, packetName);
					
			if (packetClass != null)
			{
				List<Class<?>> paramClasses = new ArrayList<>();
				
				for (Object param : params)
				{
					paramClasses.add(param.getClass());
				}				
				return packetClass.getConstructor(paramClasses.toArray(new Class<?>[0])).newInstance(params);
			}
			return null;
		} 
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant create packet named \"%s\" with params \"%s\".", packetName, Arrays.toString(params)), exception);
			return null;
		}
	}
	
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
	
	public static @Nullable Object invokeStaticMethod(VersionDependency versionDependency, @NotNull String className, @NotNull String methodName, @Nullable Object... params)
	{
		try
		{
			final Class<?> methodClass = getVersionDependentClass(versionDependency, className);
			
			if (methodClass != null)
			{
				List<Class<?>> paramClasses = new ArrayList<>();
				
				for (Object param : params)
				{
					paramClasses.add(param.getClass());
				}
				return methodClass.getMethod(methodName, paramClasses.toArray(new Class<?>[0])).invoke(null, params);
			}
			return null;
		} 
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant invoke method named \"%s\" with params \"%s\".", methodName, Arrays.toString(params)), exception);
			return null;
		}
	}
	
	// P A C K E T
	
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
			playerCon.getClass().getMethod("sendPacket", getVersionDependentClass(VersionDependency.NMS, "Packet")).invoke(playerCon, packet);
		} 
		catch (Exception exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot send packet to player \"%s\".", player), exception);
		}
	}
	
	public static void sendPacketDelayed(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull Object packet, long ticks) 
	{
		Validate.notNull((Object) player, "The plugin cannot be null.");
		
		new BukkitRunnable()
		{				
			@Override
			public void run()
			{
				sendPacket(player, packet);
			}
		}.runTaskLater(plugin, ticks);
	}
	
	public static void sendPacketDelayedAsync(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull Object packet, int ticks) 
	{
		Validate.notNull((Object) player, "The plugin cannot be null.");
		
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
	
	public static @NotNull String getJsonMessage(@NotNull String message)
	{
		Validate.notNull((Object) message, "The message cannot be null.");
		
		return StringHelper.build("{\"text\": \"", message, "\"}");
	}
	
	public static @NotNull Object getLocationToVec3D(@NotNull Location location)
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		
		final Object vec3DObject = createInstance(VersionDependency.NMS, "Vec3D", location.getX(), location.getY(), location.getZ());
		
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
	
	public static long getSecondsInTicks(int seconds)
	{
		return (long) TICK_SECOND * seconds;
	}
	
	public static void sendTime(@NotNull Player player, int ticks, int infade, int outfade)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final Object packet = createInstance(VersionDependency.NMS, "PacketPlayOutTitle", getEnumByName(VersionDependency.NMS, "PacketPlayOutTitle.EnumTitleAction", "TIMES"), null, infade, ticks, outfade);
		
		sendPacket(player, packet);
	}
	
	// D I S P L A Y S
	
	private static void sendRawTitle(@NotNull Player player, @NotNull String title, @NotNull String subTitle)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) player, "The title cannot be null.");
		Validate.notNull((Object) player, "The subtitle cannot be null.");
		
		final Object serializedTitle = invokeStaticMethod(VersionDependency.NMS, "ChatSerializer", "a", getJsonMessage(title));
		final Object serializedSubTitle = invokeStaticMethod(VersionDependency.NMS, "ChatSerializer", "a", getJsonMessage(subTitle));
		final Object packet = createInstance(VersionDependency.NMS, "PacketPlayOutTitle", getEnumByName(VersionDependency.NMS, "PacketPlayOutTitle.EnumTitleAction", "TITLE"), serializedTitle);
		final Object packet2 = createInstance(VersionDependency.NMS, "PacketPlayOutTitle", getEnumByName(VersionDependency.NMS, "PacketPlayOutTitle.EnumTitleAction", "SUBTITLE"), serializedSubTitle);
	
		sendPacket(player, packet);
		sendPacket(player, packet2);
	}
	
	public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int ticks) 
	{
		sendTitle(player, title, subtitle, ticks, TICK_SECOND, TICK_SECOND);
	}
	
	public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int ticks, int infade, int outfade) 
	{
		sendRawTitle(player, title, subtitle);
		sendTime(player, ticks, infade, outfade);
	}
	
	public static void sendGameInfo(@NotNull Player player, @NotNull String message)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) player, "The message cannot be null.");
		
		final Object serializedMessage = invokeStaticMethod(VersionDependency.NMS, "ChatSerializer", "a", getJsonMessage(message));
		final Object packet = createInstance(VersionDependency.NMS, "PacketPlayOutChat", serializedMessage, getEnumByName(VersionDependency.NMS, "ChatMessageType", "GAME_INFO"), player.getUniqueId());
		
		sendPacket(player, packet);
	}
	
	// I T E M

	public static @NotNull ItemStack getTaggedItemStack(@NotNull ItemStack itemStack, @NotNull String key, @NotNull String value) 
	{
		final Object nmsItemStack;
		final Class<?> craftItemStackClass = getVersionDependentClass(VersionDependency.CRAFTBUKKIT, "inventory.CraftItemStack");
		
		if (itemStack.getClass().equals(craftItemStackClass))
		{
			nmsItemStack = craftItemStackClass.cast(itemStack);
		}
		else
		{
			nmsItemStack = invokeStaticMethod(VersionDependency.CRAFTBUKKIT, "inventory.CraftItemStack", "asNMSCopy", itemStack);
		}
		final Object nmsItemCompound = (boolean) invokeMethod(nmsItemStack, "hasTag") ? invokeMethod(nmsItemStack, "getTag") : createInstance(VersionDependency.NMS, "NBTTagCompound");
		
		invokeMethod(nmsItemCompound, "setString", key, value);
		invokeMethod(nmsItemStack, "setTag", nmsItemCompound);
	
		final Object taggedItemStack = invokeStaticMethod(VersionDependency.CRAFTBUKKIT, "inventory.CraftItemStack", "asBukkitCopy", nmsItemStack);
		
		if (taggedItemStack != null)
		{
			return (ItemStack) taggedItemStack;
		}	
		else
		{
			throw new NullPointerException(String.format("Cannot add tag \"%s\" with key \"%s\" to itemstack \"%s\"", value, key, itemStack));
		}	
	}

	public static @Nullable String getTagFromItemStack(@NotNull ItemStack itemStack, @NotNull String key)
	{
		if (itemStack.getClass().equals(getVersionDependentClass(VersionDependency.CRAFTBUKKIT, "inventory.CraftItemStack")))
		{
			itemStack = ItemStack.class.cast(itemStack);
		}
		final Object nmsItemStack = invokeStaticMethod(VersionDependency.CRAFTBUKKIT, "inventory.CraftItemStack", "asNMSCopy", itemStack);

		if (!(boolean) invokeMethod(nmsItemStack, "hasTag"))
		{	
			return null;		
		}	
		final Object nmsItemCompound = invokeMethod(nmsItemStack, "getTag");
		final String value = (String) invokeMethod(nmsItemCompound, "getString", key);

		return value != null ? value : null;
	}
}
