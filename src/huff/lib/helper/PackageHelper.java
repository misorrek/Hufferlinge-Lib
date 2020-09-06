package huff.lib.helper;

import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R2.Vec3D;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle.EnumTitleAction;

public class PackageHelper 
{
	private static final int TICK_SECOND = 20;
	
	private PackageHelper() { }
	
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
		
		IChatBaseComponent chatTitle = ChatSerializer.a(getJsonMessage(title));
		IChatBaseComponent chatSubTitle = ChatSerializer.a(getJsonMessage(subtitle));
		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle packet2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);
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
		
		IChatBaseComponent cbc = ChatSerializer.a(getJsonMessage(message));
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO, player.getUniqueId());
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
	}
	
	// U T I L
	
	@NotNull
	public static Vec3D getLocationToVec3D(@NotNull Location location) 
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		
		return new Vec3D(location.getX(), location.getY(), location.getZ());
	}
	
	@NotNull
	public static long getSecondsInTicks(int seconds)
	{
		return (long) TICK_SECOND * seconds;
	}
	
	@NotNull
	public static String getJsonMessage(@NotNull String message)
	{
		Validate.notNull((Object) message, "The message cannot be null.");
		
		return "{\"text\": \"" + message + "\"}";
	}
	
	private static void sendTime(@NotNull Player player, int ticks, int infade, int outfade)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, infade, ticks, outfade);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	@Nullable
	private static Class<?> getNMSClass(@NotNull String className) 
	{
		Validate.notNull((Object) className, "The class-name cannot be null.");
		
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

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
}
