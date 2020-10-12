package huff.lib.helper;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketHelper 
{
	private static final int TICK_SECOND = 20;
	
	private PacketHelper() { }
	
	// G E N E R A L
	
	public static void sendPacket(@NotNull Player player, @NotNull Object packet) 
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) player, "The packet cannot be null.");
		
		try 
		{
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerCon = handle.getClass().getField("playerConnection").get(handle);
			playerCon.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerCon, packet);
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
	
	// D I S P L A Y S
	
	private static void sendRawTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) player, "The title cannot be null.");
		Validate.notNull((Object) player, "The subtitle cannot be null.");
		
		final Object chatTitle = getNMSClass("ChatSerializer").getMethod("a", String.class).invoke(null, getJsonMessage(title));
		final Object chatSubTitle = getNMSClass("ChatSerializer").getMethod("a", String.class).invoke(null, getJsonMessage(subtitle));
		final Object packet = getNMSClass("PacketPlayOutTitle").getContructor(Enum.class, chatTitle.class).newInstance(getEnumValue(getNMSClass("PacketPlayOutTitle.EnumTitleAction"), "TITLE"), chatTitle);
		final Object packet2 = getNMSClass("PacketPlayOutTitle").getContructor(Enum.class, chatTitle.class).newInstance(getEnumValue(getNMSClass("PacketPlayOutTitle.EnumTitleAction"), "SUBTITLE"), chatSubTitle);
		
		sendPacket(packet);
		sendPacket(packet2);
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
		
		final Object cbc = getNMSClass("ChatSerializer").getMethod("a", String.class).invoke(null, getJsonMessage(message));
		final Object ppoc = getNMSClass("PacketPlayOutChat").getContructor(cbc.class, Enum.class, UUID.class).newInstance(cbc, getEnumValue(getNMSClass("ChatMessageType"), "GAME_INFO"), player.getUniqueId());
		
		sendPacket(ppoc);
	}
	
	// I T E M

	public static @NotNull ItemStack addTagToItemStack(@NotNull ItemStack itemStack, @NotNull String key, @NotNull String value)
	{
		final Object nmsItemStack = getNMSClass("CraftItemStack").getMethod("asNMSCopy", itemStack.class).invoke(null, itemStack);
		final Object nmsItemCompound = nmsItemStack.class.getMethod("hasTag").invoke(nmsItemStack) ? nmsItemStack.class.getMethod("getTag").invoke(nmsItemStack) : getNMSClass("NBTTagCompound").getConstructor().newInstance();

		nmsItemCompound.class.getMethod("set", String.class, getNMSClass("NBTTagBytes").class).invoke(nmsItemCompound, key, getNMSClass("NBTTagBytes").getConstructor(Bytes[].class).newInstance(value.getBytes()));
		nmsItemStack.class.getMethod("setTag", nmsItemCompound.class).invoke(nmsItemStack, nmsItemCompound);

		getNMSClass("CraftItemStack").getMethod("asBukkitCopy", nmsItemStack.class).invoke(null, nmsItemStack);
	}

	public static @Nullable String getTagFromItemStack(@NotNull ItemStack itemStack, @NotNull String key)
	{
		final Object nmsItemStack = getNMSClass("CraftItemStack").getMethod("asNMSCopy", itemStack.class).invoke(null, itemStack);
	
		if (!nmsItemStack.class.getMethod("hasTag"))
		{	
			return null;		
		}	
		final Object nmsItemCompound = nmsItemStack.class.getMethod("getTag").invoke(nmsItemStack);
		final Bytes[] bytes = nmsItemCompound.class.getMethod("getBytes", String.class).invoke(nmsItemCompound, key);

		return bytes != null ? new String(bytes) : null;
	}

	// U T I L
	
	public static @NotNull Object getLocationToVec3D(@NotNull Location location) 
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		
		return getNMSClass("Vec3D").getConstructor(Double.class, Double.class, Double.class).invoke(location.getX(), location.getY(), location.getZ());
	}
	
	public static long getSecondsInTicks(int seconds)
	{
		return (long) TICK_SECOND * seconds;
	}
	
	public static @NotNull String getJsonMessage(@NotNull String message)
	{
		Validate.notNull((Object) message, "The message cannot be null.");
		
		return "{\"text\": \"" + message + "\"}";
	}
	
	private static void sendTime(@NotNull Player player, int ticks, int infade, int outfade)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, infade, ticks, outfade);
		
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
		
	private static @Nullable Class<?> getNMSClass(@NotNull String className) 
	{
		Validate.notNull((Object) className, "The class-name cannot be null.");
		
		final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try 
		{
			return Class.forName("net.minecraft.server." + version + "." + className);
		} 
		catch (ClassNotFoundException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cant get nms-class named \"%s\" in version \"%s\".", className, version), exception);
			return null;
		}
	}

	private static @Nullable <E extends Enum> E getEnumValue(Class<E> enumClass, String value) throws NoSuchFieldException, IllegalAccessException 
	{
		E[] values = getEnumValues(enumClass);

		for (int i = 0; i < values.length; i++)
		{
			E currentValue = values[i];

			if (currentValue.toString().equals(value))
			{
				return currentValue;
			}
		}
	}

	private static @Nullable <E extends Enum> E[] getEnumValues(Class<E> enumClass) throws NoSuchFieldException, IllegalAccessException 
	{
		Field f = enumClass.getDeclaredField("$VALUES");
		System.out.println(f);
		System.out.println(Modifier.toString(f.getModifiers()));
		f.setAccessible(true);
		Object o = f.get(null);
		return (E[]) o;
	}
}
